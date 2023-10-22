package cn.bugstack.xfg.dev.tech.domain.zsxq.service;

import cn.bugstack.chatglm.model.*;
import cn.bugstack.chatglm.session.OpenAiSession;
import cn.bugstack.xfg.dev.tech.domain.zsxq.adapter.IZSXQAdapter;
import cn.bugstack.xfg.dev.tech.domain.zsxq.model.vo.TopicsItemVO;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 只能回帖
 * @create 2023-10-22 10:10
 */
@Slf4j
@Service
public class AiReply implements IAiReply {

    @Autowired(required = false)
    private OpenAiSession openAiSession;
    @Resource
    private IZSXQAdapter zsxqAdapter;
    @Value("${zsxq.config.user-id}")
    private Long userId;

    private final String regex = "<e type=\"mention\" uid=\"(\\d+)\" title=\"(.*?)\" /> (.*)";
    private volatile Set<Long> topicIds = new HashSet<>();

    @Override
    public void doAiReply() {
        List<TopicsItemVO> topicsItemVOS = zsxqAdapter.queryTopics();

        for (TopicsItemVO topicsItem : topicsItemVOS) {
            // 是否回答过判断
            if (!isCommentDone(topicsItem)) continue;
            // 找到圈我我帖子
            long topicId = topicsItem.getTopicId();
            String text = topicsItem.getTalk();

            // "<e type="mention" uid="241858242255511" title="%40%E5%B0%8F%E5%82%85%E5%93%A5" /> 提问 java 冒泡排序"
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);

            if (!matcher.find()) continue;
            String uid = matcher.group(1);
            String remainingText = matcher.group(3);

            if (this.userId.equals(Long.valueOf(uid))) {

                if (null == openAiSession) {
                    log.info("你没有开启 ChatGLM 参考yml配置文件来开启");
                    // 你可以使用 ChatGLM SDK 进行回答，回复问题；
                    zsxqAdapter.comment(topicId, "【测试，只回答圈我的帖子】对接 ChatGLM SDK https://bugstack.cn/md/project/chatgpt/sdk/chatglm-sdk-java.html 回答：" + remainingText);
                } else {
                    log.info("ChatGLM 进入回答 {} {}", topicId, remainingText);
                    if (topicIds.contains(topicId)) {
                        continue;
                    } else {
                        topicIds.add(topicId);
                    }
                    new Thread(() -> {
                        // 入参；模型、请求信息
                        ChatCompletionRequest request = new ChatCompletionRequest();
                        request.setModel(Model.CHATGLM_LITE); // chatGLM_6b_SSE、chatglm_lite、chatglm_lite_32k、chatglm_std、chatglm_pro
                        request.setPrompt(new ArrayList<ChatCompletionRequest.Prompt>() {
                            private static final long serialVersionUID = -7988151926241837899L;

                            {
                                add(ChatCompletionRequest.Prompt.builder()
                                        .role(Role.user.getCode())
                                        .content(remainingText)
                                        .build());
                            }
                        });

                        // 请求
                        try {
                            StringBuilder content = new StringBuilder();
                            openAiSession.completions(request, new EventSourceListener() {
                                @Override
                                public void onEvent(EventSource eventSource, @Nullable String id, @Nullable String type, String data) {
                                    ChatCompletionResponse chatCompletionResponse = com.alibaba.fastjson.JSON.parseObject(data, ChatCompletionResponse.class);
                                    log.info("测试结果 onEvent：{}", chatCompletionResponse.getData());
                                    // type 消息类型，add 增量，finish 结束，error 错误，interrupted 中断
                                    if (EventType.finish.getCode().equals(type)) {
                                        ChatCompletionResponse.Meta meta = com.alibaba.fastjson.JSON.parseObject(chatCompletionResponse.getMeta(), ChatCompletionResponse.Meta.class);
                                        log.info("[输出结束] Tokens {}", com.alibaba.fastjson.JSON.toJSONString(meta));
                                    }
                                    content.append(chatCompletionResponse.getData());
                                }

                                @Override
                                public void onClosed(EventSource eventSource) {
                                    log.info("对话完成");

                                    // 你可以使用 ChatGLM SDK 进行回答，回复问题；
                                    String contents = "ChatGLM 回答：" + content;
                                    int maxLength = 5000;
                                    int contentLength = contents.length();
                                    int startIndex = 0;
                                    int endIndex = maxLength;

                                    while (startIndex < contentLength) {
                                        if (endIndex > contentLength) {
                                            endIndex = contentLength;
                                        }

                                        String subContent = contents.substring(startIndex, endIndex);
                                        zsxqAdapter.comment(topicId, subContent);

                                        startIndex = endIndex;
                                        endIndex += maxLength;
                                    }

                                    topicIds.remove(topicId);
                                }

                            });
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }).start();
                }

            }

        }

        log.info("AI回复：{}", JSON.toJSONString(topicsItemVOS));
    }

    private boolean isCommentDone(TopicsItemVO topicsItem) {
        List<TopicsItemVO.ShowCommentsItem> showComments = topicsItem.getShowCommentsItems();
        if (null == showComments || showComments.isEmpty()) return true;
        for (TopicsItemVO.ShowCommentsItem item : topicsItem.getShowCommentsItems()) {
            long userId = item.getUserId();
            if (this.userId == userId) {
                return false;
            }
        }
        return true;
    }

}

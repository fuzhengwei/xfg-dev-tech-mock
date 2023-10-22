package cn.bugstack.xfg.dev.tech.test;

import cn.bugstack.chatglm.model.ChatCompletionRequest;
import cn.bugstack.chatglm.session.OpenAiSession;
import cn.bugstack.xfg.dev.tech.domain.zsxq.adapter.IZSXQAdapter;
import cn.bugstack.xfg.dev.tech.domain.zsxq.model.vo.TopicsItemVO;
import cn.bugstack.xfg.dev.tech.domain.zsxq.service.IAiReply;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description Mock 测试
 * @create 2023-10-22 11:20
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MockTest {

    @Resource
    private IAiReply aiReply;

    @MockBean
    private IZSXQAdapter izsxqAdapter;

    @Test
    public void test_doAiReply_() throws InterruptedException, JsonProcessingException {
        Mockito.when(izsxqAdapter.queryTopics()).thenReturn(new ArrayList<TopicsItemVO>() {{
            TopicsItemVO topicsItemVO = new TopicsItemVO();
            topicsItemVO.setTopicId(10001L);
            topicsItemVO.setTalk("<e type=\"mention\" uid=\"241858242255511\" title=\"%40%E5%B0%8F%E5%82%85%E5%93%A5\" /> 提问 java 冒泡排序");
            add(topicsItemVO);
        }});

        Mockito.when(izsxqAdapter.comment(Mockito.anyLong(), Mockito.anyString())).thenReturn(true);

        aiReply.doAiReply();

        // 等待；ChatGLM 异步回复
        new CountDownLatch(1).await();
    }

}

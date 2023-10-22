package cn.bugstack.xfg.dev.tech.infrastructure.gateway.adapter;

import cn.bugstack.xfg.dev.tech.domain.zsxq.adapter.IZSXQAdapter;
import cn.bugstack.xfg.dev.tech.domain.zsxq.model.vo.TopicsItemVO;
import cn.bugstack.xfg.dev.tech.infrastructure.gateway.api.IZSXQApi;
import cn.bugstack.xfg.dev.tech.infrastructure.gateway.dto.RespData;
import cn.bugstack.xfg.dev.tech.infrastructure.gateway.dto.ResponseDTO;
import cn.bugstack.xfg.dev.tech.infrastructure.gateway.dto.TopicsItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 知识星球适配器接口
 * @create 2023-10-22 10:18
 */
@Slf4j
@Service
public class ZSXQAdapter implements IZSXQAdapter {

    @Resource
    private IZSXQApi zsxqApi;

    @Override
    public List<TopicsItemVO> queryTopics() {
        try {
            ResponseDTO responseDTO = zsxqApi.topics();
            RespData respData = responseDTO.getRespData();
            List<TopicsItem> topics = respData.getTopics();
            List<TopicsItemVO> topicsItemVOList = new ArrayList<>();

            for (TopicsItem topicsItem : topics) {
                TopicsItemVO topicsItemVO = TopicsItemVO.builder()
                        .topicId(topicsItem.getTopicId())
                        .talk(topicsItem.getTalk().getText())
                        .showCommentsItems(topicsItem.getShowComments() != null ? topicsItem.getShowComments().stream()
                                .map(showCommentsItem -> {
                                    TopicsItemVO.ShowCommentsItem item = new TopicsItemVO.ShowCommentsItem();
                                    item.setUserId(showCommentsItem.getOwner().getUserId());
                                    return item;
                                })
                                .collect(Collectors.toList()) : new ArrayList<>())
                        .build();

                topicsItemVOList.add(topicsItemVO);
            }

            return topicsItemVOList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void comment(long topicId, String content) {
        zsxqApi.comment(topicId, content);
    }

}

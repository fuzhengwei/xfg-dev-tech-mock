package cn.bugstack.xfg.dev.tech.domain.zsxq.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 话题
 * @create 2023-10-22 10:13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopicsItemVO {

    private long topicId;

    private String talk;

    private List<ShowCommentsItem> showCommentsItems;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ShowCommentsItem {
        private long userId;
    }

}

package cn.bugstack.xfg.dev.tech.domain.zsxq.adapter;

import cn.bugstack.xfg.dev.tech.domain.zsxq.model.vo.TopicsItemVO;

import java.util.List;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 知识星球接口适配
 * @create 2023-10-22 10:11
 */
public interface IZSXQAdapter {

    List<TopicsItemVO> queryTopics();

    void comment(long topicId, String content);

}

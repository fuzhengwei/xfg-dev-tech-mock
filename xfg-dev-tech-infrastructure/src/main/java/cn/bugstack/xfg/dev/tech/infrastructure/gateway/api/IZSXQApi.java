package cn.bugstack.xfg.dev.tech.infrastructure.gateway.api;

import cn.bugstack.xfg.dev.tech.infrastructure.gateway.dto.ResponseDTO;

import java.io.IOException;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 知识星球API接口
 * @create 2023-10-22 09:47
 */
public interface IZSXQApi {

    /**
     * 查询知识星球帖子内容
     *
     * @return 帖子数据
     * @throws IOException 异常
     */
    ResponseDTO topics() throws IOException;

    /**
     * 回复帖子
     *
     * @param topicId 帖子ID
     * @param content 回复内容
     */
    void comment(long topicId, String content);

}

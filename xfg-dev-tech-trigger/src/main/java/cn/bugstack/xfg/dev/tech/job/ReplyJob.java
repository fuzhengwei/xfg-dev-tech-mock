package cn.bugstack.xfg.dev.tech.job;

import cn.bugstack.xfg.dev.tech.domain.zsxq.service.IAiReply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 回帖JOB
 * @create 2023-10-22 11:02
 */
@Slf4j
@Component()
public class ReplyJob {

    @Resource
    private IAiReply aiReply;

    @Scheduled(cron = "0/10 * * * * ?")
    public void exec() throws Exception {
        aiReply.doAiReply();
    }

}

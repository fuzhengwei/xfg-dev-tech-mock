package cn.bugstack.xfg.dev.tech.test;

import cn.bugstack.xfg.dev.tech.domain.zsxq.service.IAiReply;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 单元测试
 * @create 2023-10-22 09:08
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    @Resource
    private IAiReply aiReply;

    @Test
    public void test_IAiReply() {
        aiReply.doAiReply();


    }

}

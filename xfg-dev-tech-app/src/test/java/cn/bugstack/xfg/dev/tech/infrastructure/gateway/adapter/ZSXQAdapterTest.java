package cn.bugstack.xfg.dev.tech.infrastructure.gateway.adapter;

import cn.bugstack.xfg.dev.tech.domain.zsxq.model.vo.TopicsItemVO;
import cn.bugstack.xfg.dev.tech.infrastructure.gateway.api.IZSXQApi;
import cn.bugstack.xfg.dev.tech.infrastructure.gateway.dto.*;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class ZSXQAdapterTest {

    @Mock
    private IZSXQApi mockZsxqApi;

    @InjectMocks
    private ZSXQAdapter zsxqAdapterUnderTest;

    @Test
    public void testQueryTopics() throws Exception {
        // Setup
        final List<TopicsItemVO> expectedResult = Arrays.asList(TopicsItemVO.builder()
                .topicId(0L)
                .talk("talk")
                .showCommentsItems(Arrays.asList(TopicsItemVO.ShowCommentsItem.builder()
                        .userId(0L)
                        .build()))
                .build());

        // Configure IZSXQApi.topics(...).
        final ResponseDTO responseDTO = new ResponseDTO();
        final RespData respData = new RespData();
        final TopicsItem topicsItem = new TopicsItem();
        final ShowCommentsItem showCommentsItem = new ShowCommentsItem();
        final Owner owner = new Owner();
        owner.setUserId(0L);
        showCommentsItem.setOwner(owner);
        topicsItem.setShowComments(Arrays.asList(showCommentsItem));
        final Talk talk = new Talk();
        talk.setText("talk");
        topicsItem.setTalk(talk);
        topicsItem.setTopicId(0L);
        respData.setTopics(Arrays.asList(topicsItem));
        responseDTO.setRespData(respData);
        when(mockZsxqApi.topics()).thenReturn(responseDTO);

        // Run the test
        final List<TopicsItemVO> result = zsxqAdapterUnderTest.queryTopics();

        // Verify the results
        assertEquals(expectedResult, result);

        log.info("测试结果：{}", JSON.toJSONString(result));
    }

    @Test
    public void testQueryTopics_IZSXQApiThrowsIOException() throws Exception {
        // Setup
        when(mockZsxqApi.topics()).thenThrow(IOException.class);

        // Run the test
        assertThrows(RuntimeException.class, () -> zsxqAdapterUnderTest.queryTopics());
    }

    @Test
    public void testComment() {
        // Setup
        // Run the test
        final boolean result = zsxqAdapterUnderTest.comment(0L, "content");

        // Verify the results
        assertTrue(result);
        verify(mockZsxqApi).comment(0L, "content");
    }
}

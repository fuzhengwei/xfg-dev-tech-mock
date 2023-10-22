package cn.bugstack.xfg.dev.tech.infrastructure.gateway.api.impl;

import cn.bugstack.xfg.dev.tech.infrastructure.gateway.api.IZSXQApi;
import cn.bugstack.xfg.dev.tech.infrastructure.gateway.dto.ResponseDTO;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 知识星球Api实现
 * @create 2023-10-22 09:53
 */
@Slf4j
@Component
public class ZSXQApi implements IZSXQApi {

    @Value("${zsxq.config.id}")
    private String id;
    @Value("${zsxq.config.cookie}")
    private String cookie;

    @Override
    public ResponseDTO topics() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpGet get = new HttpGet("https://api.zsxq.com/v2/groups/" + id + "/topics?scope=all&count=20");

        get.addHeader("Accept-Encoding", "deflate, gzip");
        get.addHeader("accept", "application/json, text/plain, */*");
        get.addHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8");
        get.addHeader("authority", "api.zsxq.com");
        get.addHeader("cookie", cookie);
        get.addHeader("dnt", "1");
        get.addHeader("origin", "https://wx.zsxq.com");
        get.addHeader("referer", "https://wx.zsxq.com/");
        get.addHeader("sec-ch-ua", "\"Chromium\";v=\"118\", \"Google Chrome\";v=\"118\", \"Not=A?Brand\";v=\"99\"");
        get.addHeader("sec-ch-ua-mobile", "?0");
        get.addHeader("sec-ch-ua-platform", "\"macOS\"");
        get.addHeader("sec-fetch-dest", "empty");
        get.addHeader("sec-fetch-mode", "cors");
        get.addHeader("sec-fetch-site", "same-site");
        get.addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36");
        get.addHeader("x-request-id", "372177b46-4e7d-9373-d891-98a22adaeb7");
        get.addHeader("x-signature", "32b39b5d1af5995e3b5022e58a8d8f23cd427434");
        get.addHeader("x-timestamp", "1697249698");
        get.addHeader("x-version", "2.45.0");

        CloseableHttpResponse response = httpClient.execute(get);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String jsonStr = EntityUtils.toString(response.getEntity());
            return JSON.parseObject(jsonStr, ResponseDTO.class);
        } else {
            throw new RuntimeException("Err Code is " + response.getStatusLine().getStatusCode());
        }
    }

    @Override
    public void comment(long topicId, String content) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost httpPost = new HttpPost("https://api.zsxq.com/v2/topics/" + topicId + "/comments");

        httpPost.setHeader("Accept-Encoding", "deflate, gzip");
        httpPost.setHeader("accept", "application/json, text/plain, */*");
        httpPost.setHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8");
        httpPost.setHeader("authority", "api.zsxq.com");
        httpPost.setHeader("content-type", "application/json");
        httpPost.setHeader("cookie", cookie);
        httpPost.setHeader("dnt", "1");
        httpPost.setHeader("origin", "https://wx.zsxq.com");
        httpPost.setHeader("referer", "https://wx.zsxq.com/");
        httpPost.setHeader("sec-ch-ua", "\"Chromium\";v=\"118\", \"Google Chrome\";v=\"118\", \"Not=A?Brand\";v=\"99\"");
        httpPost.setHeader("sec-ch-ua-mobile", "?0");
        httpPost.setHeader("sec-ch-ua-platform", "\"macOS\"");
        httpPost.setHeader("sec-fetch-dest", "empty");
        httpPost.setHeader("sec-fetch-mode", "cors");
        httpPost.setHeader("sec-fetch-site", "same-site");
        httpPost.setHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36");
        httpPost.setHeader("x-request-id", "162ae5f17-2123-4ae3-67df-8b9775414e0");
        httpPost.setHeader("x-signature", "698895e3ec4e651128b3d16755546bd2bc659687");
        httpPost.setHeader("x-timestamp", "1697257286");
        httpPost.setHeader("x-version", "2.45.0");

        String requestBody = "{\"req_data\":{\"text\":\"" + StringEscapeUtils.escapeJava(content) + "\",\"image_ids\":[],\"mentioned_user_ids\":[]}}";
        try {
            httpPost.setEntity(new StringEntity(requestBody));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity);
            log.info("回贴结果 {}", responseString);
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
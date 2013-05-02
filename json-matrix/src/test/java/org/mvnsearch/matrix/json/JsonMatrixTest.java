package org.mvnsearch.matrix.json;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import junit.framework.TestCase;
import org.codehaus.jackson.map.ObjectMapper;
import org.perf4j.StopWatch;

import java.util.Date;

/**
 * json matrix test
 *
 * @author linux_china
 */
public class JsonMatrixTest extends TestCase {
    /**
     * object mapper
     */
    private ObjectMapper objectMapper = new ObjectMapper();
    /**
     * object mapper from jackson 2
     */
    private com.fasterxml.jackson.databind.ObjectMapper objectMapper2 = new com.fasterxml.jackson.databind.ObjectMapper();
    /**
     * gson object
     */
    private Gson gson = new Gson();

    /**
     * spike test
     *
     * @throws Exception exception
     */
    public void testSpike() throws Exception {
        ChatMessage msg = constructMessage();
        String json = JSON.toJSONString(msg);
        ChatMessage chatMessage = JSON.parseObject(json, ChatMessage.class);
        System.out.println(chatMessage.getBody());
    }

    /**
     * matrix test
     *
     * @throws Exception exception
     */
    public void testMatrix() throws Exception {
        ChatMessage msg = constructMessage();
        int count = 100000;
        //hot swap
        for (int i = 0; i < 10000; i++) {
            jackson(msg);
            jackson2(msg);
            gson(msg);
            fastJson(msg);
        }
        System.out.println(count + " loop:");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("jackson");
        for (int i = 0; i < count; i++) {
            jackson(msg);
        }
        stopWatch.stop("jackson");
        System.out.println(stopWatch.getTag() + ":" + stopWatch.getElapsedTime());
        stopWatch.start("jackson2");
        for (int i = 0; i < count; i++) {
            jackson2(msg);
        }
        stopWatch.stop("jackson2");
        System.out.println(stopWatch.getTag() + ":" + stopWatch.getElapsedTime());
        stopWatch.start("gson");
        for (int i = 0; i < count; i++) {
            gson(msg);
        }
        stopWatch.stop("gson");
        System.out.println(stopWatch.getTag() + ":" + stopWatch.getElapsedTime());
        stopWatch.start("fastjson");
        for (int i = 0; i < count; i++) {
            fastJson(msg);
        }
        stopWatch.stop("fastjson");
        System.out.println(stopWatch.getTag() + ":" + stopWatch.getElapsedTime());
    }

    /**
     * json protocol with jackson
     *
     * @param message message
     * @return chat message
     * @throws Exception exception
     */
    public ChatMessage jackson(ChatMessage message) throws Exception {
        String jsonText = objectMapper.writeValueAsString(message);
        return objectMapper.readValue(jsonText, ChatMessage.class);
    }

    /**
     * json protocol with jackson2
     *
     * @param message message
     * @return chat message
     * @throws Exception exception
     */
    public ChatMessage jackson2(ChatMessage message) throws Exception {
        String jsonText = objectMapper2.writeValueAsString(message);
        return objectMapper2.readValue(jsonText, ChatMessage.class);
    }

    /**
     * gson
     *
     * @param message message
     * @return chat message
     * @throws Exception exception
     */
    public ChatMessage gson(ChatMessage message) throws Exception {
        String json = gson.toJson(message);
        return gson.fromJson(json, ChatMessage.class);
    }

    /**
     * fast json
     *
     * @param message chat message
     * @return chat message
     * @throws Exception exception
     */
    public ChatMessage fastJson(ChatMessage message) throws Exception {
        String json = JSON.toJSONString(message);
        return JSON.parseObject(json, ChatMessage.class);
    }

    /**
     * construct message
     *
     * @return chat message
     */
    private ChatMessage constructMessage() {
        ChatMessage msg = new ChatMessage();
        msg.setId(11343L);
        msg.setUserId(1134);
        msg.setUserNick("linux_china");
        msg.setCreatedAt(new Date());
        msg.setScore(2.0042);
        msg.setBody("这是聊天的内容");
        msg.getHeaders().put("contentType", "text/plain");
        msg.getHeaders().put("room", "123423");
        msg.setContentLength(120);
        return msg;
    }
}

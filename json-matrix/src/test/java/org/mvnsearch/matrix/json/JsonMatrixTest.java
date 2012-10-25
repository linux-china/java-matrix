package org.mvnsearch.matrix.json;

import junit.framework.TestCase;
import org.codehaus.jackson.map.ObjectMapper;

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
    com.fasterxml.jackson.databind.ObjectMapper objectMapper2 = new com.fasterxml.jackson.databind.ObjectMapper();

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
        }
        System.out.println(count + " loop:");
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            jackson(msg);
        }
        long end1 = System.currentTimeMillis();
        System.out.println("jackson:" + (end1 - start));
        for (int i = 0; i < count; i++) {
            jackson2(msg);
        }
        long end2 = System.currentTimeMillis();
        System.out.println("jackson2:" + (end2 - end1));
    }

    /**
     * json protocol with jackson
     *
     * @param message message
     * @return chat message
     * @throws Exception exception
     */
    public ChatMessage jackson(ChatMessage message) throws Exception {
        byte[] jsonText = objectMapper.writeValueAsBytes(message);
        ChatMessage msg = objectMapper.readValue(jsonText, ChatMessage.class);
        msg.setContentLength(jsonText.length);
        return msg;
    }

    /**
     * json protocol with jackson2
     *
     * @param message message
     * @return chat message
     * @throws Exception exception
     */
    public ChatMessage jackson2(ChatMessage message) throws Exception {
        byte[] jsonText = objectMapper2.writeValueAsBytes(message);
        ChatMessage msg = objectMapper2.readValue(jsonText, ChatMessage.class);
        msg.setContentLength(jsonText.length);
        return msg;
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

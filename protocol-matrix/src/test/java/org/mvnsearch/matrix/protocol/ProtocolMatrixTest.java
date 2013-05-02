package org.mvnsearch.matrix.protocol;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import junit.framework.TestCase;
import org.codehaus.jackson.map.ObjectMapper;
import org.msgpack.MessagePack;
import org.perf4j.StopWatch;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;

/**
 * protocol matrix test
 *
 * @author linux_china
 */
public class ProtocolMatrixTest extends TestCase {
    /**
     * object mapper
     */
    private ObjectMapper objectMapper = new ObjectMapper();
    /**
     * object mapper from jackson 2
     */
    com.fasterxml.jackson.databind.ObjectMapper objectMapper2 = new com.fasterxml.jackson.databind.ObjectMapper();
    /**
     * message pack
     */
    private MessagePack msgpack = new MessagePack();

    /**
     * spike test
     *
     * @throws Exception exception
     */
    public void testSpike() throws Exception {
        ChatMessage msg = constructMessage();
        System.out.println("Hessian Content Length:" + hessian(msg).getContentLength());
        System.out.println("Jackson Content Length:" + jackson(msg).getContentLength());
        System.out.println("Jackson2 Content Length:" + jackson2(msg).getContentLength());
        System.out.println("MessagePack Content Length:" + msgpack(msg).getContentLength());
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
            hessian(msg);
            jackson(msg);
            jackson2(msg);
            msgpack(msg);
        }
        System.out.println(count + " loop:");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("hessian");
        for (int i = 0; i < count; i++) {
            hessian(msg);
        }
        stopWatch.stop("hessian");
        System.out.println(stopWatch.getTag() + ":" + stopWatch.getElapsedTime());
        stopWatch.start("jackson");
        for (int i = 0; i < count; i++) {
            jackson(msg);
        }
        stopWatch.stop("jackson");
        System.out.println(stopWatch.getTag() + ":" + stopWatch.getElapsedTime());
        stopWatch.start("msgpack");
        for (int i = 0; i < count; i++) {
            msgpack(msg);
        }
        stopWatch.stop("msgpack");
        System.out.println(stopWatch.getTag() + ":" + stopWatch.getElapsedTime());
        stopWatch.start("jackson2");
        for (int i = 0; i < count; i++) {
            jackson2(msg);
        }
        stopWatch.stop("jackson2");
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
     * hessian protocol
     *
     * @param message message
     * @return chat message
     * @throws Exception exception
     */
    public ChatMessage hessian(ChatMessage message) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Hessian2Output output = new Hessian2Output(bos);
        output.writeObject(message);
        output.flush();
        byte[] content = bos.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(content);
        Hessian2Input input = new Hessian2Input(bis);
        ChatMessage msg = (ChatMessage) input.readObject(ChatMessage.class);
        msg.setContentLength(content.length);
        return msg;
    }

    /**
     * message pack
     *
     * @param message message
     * @return chat message
     * @throws Exception exception
     */
    public ChatMessage msgpack(ChatMessage message) throws Exception {
        byte[] content = msgpack.write(message);
        ChatMessage msg = msgpack.read(content, ChatMessage.class);
        msg.setContentLength(content.length);
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

package org.mvnsearch.matrix.protocol;

import org.msgpack.annotation.Message;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * chat message
 *
 * @author linux_china
 */
@Message
public class ChatMessage implements Serializable {
    /**
     * id
     */
    public Long id;
    /**
     * user id
     */
    public Integer userId;
    /**
     * user nick
     */
    public String userNick;
    /**
     * body
     */
    public String body;
    /**
     * score
     */
    public Double score;
    /**
     * created timestmap
     */
    public Date createdAt;
    /**
     * content length
     */
    public Integer contentLength;
    /**
     * headers
     */
    public Map<String, String> headers = new HashMap<String, String>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Integer getContentLength() {
        return contentLength;
    }

    public void setContentLength(Integer contentLength) {
        this.contentLength = contentLength;
    }
}

package com.myim.common.entity;

import java.util.UUID;

/**
 * <p>
 * ChatMessage
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/8/1
 */
public class ChatMessage {

    private User user;
    private Long messageId;
    private String from;
    private String to;
    private String content;
    private Long time;
    private String url;
    private ContentType contentType = ContentType.TEXT;

    public enum ContentType{
        TEXT,AUDIO,VIDEO,OTHER
    }


    @Override
    public String toString() {
        return "ChatMessage{" +
                "user=" + user +
                ", messageId=" + messageId +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", content='" + content + '\'' +
                ", time=" + time +
                '}';
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }
}

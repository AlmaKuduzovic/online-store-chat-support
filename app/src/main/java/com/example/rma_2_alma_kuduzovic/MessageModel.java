package com.example.rma_2_alma_kuduzovic;

public class MessageModel {

    private String messageId;
    private String senderId;
    private String message;
    private long sendTime;

    public MessageModel(String messageId, String senderId, String message, long sendTime) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.message = message;
        this.sendTime = sendTime;
    }

    public MessageModel() {
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }
}

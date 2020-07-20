package com.hank.news.netty.privateprotocolstack;

/**
 * 消息类型
 */
public enum  MessageType {


    //登录请求
    LOGIN_REQ((byte)3),
    //登录应答
    LOGIN_RESP((byte) 4),
    //心跳请求
    HEART_BEAT_REQ((byte) 5),
    //心跳应答
    HEART_BEAT_RESP((byte)6);

    public byte value;

    MessageType(byte value) {
        this.value = value;
    }

    MessageType(int i) {
    }


    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

}

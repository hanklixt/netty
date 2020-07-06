package com.hank.news.netty.privateprotocolstack;

public enum  MessageType {

    LOGIN_REQ((byte)3),
    LOGIN_RESP((byte) 4),
    HEART_BEAT_REQ((byte) 5),
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

package com.hank.news.netty.privateprotocolstack;

public final class NettyMessage {

    /**
     * 消息头
     */
    private Header header;

    /**
     * body
     */
    private Object body;


    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "NettyMessage{" +
                "header=" + header +
                ", body=" + body +
                '}';
    }

}

package com.hank.news.netty.javaserialize;

import java.io.Serializable;

/**
 * @Description
 * @auther 李先涛
 * @create 2020-06-28 15:47
 */
public class SubscribeResp implements Serializable {

    /**
     * 订购编号
     */
    private Integer SubReqId;

    /**
     * 订购回应，0表示成功
     */
    private Integer respCode;

    /**
     * 可选商品描述
     */
    private String desc;

    public Integer getSubReqId() {
        return SubReqId;
    }

    public void setSubReqId(Integer subReqId) {
        SubReqId = subReqId;
    }

    public Integer getRespCode() {
        return respCode;
    }

    public void setRespCode(Integer respCode) {
        this.respCode = respCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    @Override
    public String toString() {
        return "SubscribeResp{" +
                "SubReqId=" + SubReqId +
                ", respCode=" + respCode +
                ", desc='" + desc + '\'' +
                '}';
    }
}

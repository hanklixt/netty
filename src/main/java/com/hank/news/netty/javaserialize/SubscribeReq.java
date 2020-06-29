package com.hank.news.netty.javaserialize;

import java.io.Serializable;

/**
 * @Description
 * @auther 李先涛
 * @create 2020-06-28 15:43
 */
public class SubscribeReq implements Serializable {

    /**
     * 订购编号
     */
    private Integer subReqId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 产品名称
     */
    private String productName;


    /**
     * 订购者电话号码
     */
    private String phoneNumber;

    /**
     * 订购者地址
     */
    private String address;

    public Integer getSubReqId() {
        return subReqId;
    }

    public void setSubReqId(Integer subReqId) {
        this.subReqId = subReqId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "SubscribeReq{" +
                "subReqId=" + subReqId +
                ", userName='" + userName + '\'' +
                ", productName='" + productName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}

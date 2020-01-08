package com.hank.netty.netty.io.action;

/**
 * @author lxt
 * @date 2020-01-07-11:34
 */
public class Data {
    private int m;
    private int n;

    public Data(int m, int n) {
        this.m = m;
        this.n = n;
    }

    @Override
    public String toString() {
        int r = n / m;
        return n + "/" + m + "=" + r;
    }
}

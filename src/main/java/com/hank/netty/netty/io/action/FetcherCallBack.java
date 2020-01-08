package com.hank.netty.netty.io.action;

/**
 * @author lxt
 * @date 2020-01-07-11:37
 */
public interface FetcherCallBack {

    void onData(Data data) throws Exception;

    void onError(Throwable throwable);
}

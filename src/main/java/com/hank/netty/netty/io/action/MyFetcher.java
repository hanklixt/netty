package com.hank.netty.netty.io.action;

/**
 * @author lxt
 * @date 2020-01-07-11:44
 */
public class MyFetcher implements Fetcher {

    final Data data;

    public MyFetcher(Data data) {
        this.data = data;
    }

    @Override
    public void fetcherData(FetcherCallBack callBack) {
        try {
            callBack.onData(data);
        } catch (Exception e) {
            callBack.onError(e);
        }
    }
}

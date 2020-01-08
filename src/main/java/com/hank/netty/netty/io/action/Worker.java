package com.hank.netty.netty.io.action;

/**
 * @author lxt
 * @date 2020-01-07-11:46
 * 基于回调的简单使用
 */
public class Worker {

    public static void main(String[] args) {
        doWork();
    }

    private static void doWork() {
        final Fetcher fetcher = new MyFetcher(new Data(0, 1));
        fetcher.fetcherData(new FetcherCallBack() {
            @Override
            public void onData(Data data) throws Exception {
                System.out.println(data.toString());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }
        });
    }

}

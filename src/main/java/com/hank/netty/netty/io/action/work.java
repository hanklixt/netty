package com.hank.netty.netty.io.action;

/**
 * @Description
 * @auther 李先涛
 * @create 2020-06-24 17:48
 */
public class work implements Fetcher {


    @Override
    public void fetcherData(FetcherCallBack callBack) {
        try {
            callBack.onData(new Data(1,1));
        } catch (Exception e) {
           callBack.onError(e);
        }
    }
    public static void main(String[] args) {
        work work = new work();
        work.fetcherData(new FetcherCallBack() {
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

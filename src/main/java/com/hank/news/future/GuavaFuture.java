package com.hank.news.future;

import com.google.common.util.concurrent.*;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lxt
 * @create 2020-10-28 10:24
 */
public class GuavaFuture {

    //烧水任务
    static class HotWaterJob implements Callable<Boolean>{

        @Override
        public Boolean call() throws Exception {
            System.out.println("打水");
            System.out.println("加水");
            System.out.println("放到火上烧");
            return true;
        }
    }

    //洗水壶任务
    static class WashJob implements Callable<Boolean>{

        @Override
        public Boolean call() throws Exception {
            System.out.println("找茶具");
            System.out.println("洗茶具");
            System.out.println("晾干");
            return true;
        }
    }

    static class MainJob implements Runnable {
        boolean warterOk = false;
        boolean cupOk = false;
        int gap = 500 / 10;
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(gap);
                    System.out.println("读书中......");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (warterOk&&cupOk) {
                    drinkTea(warterOk, cupOk);
                }
            }
        }
        public void drinkTea(Boolean wOk, Boolean cOK) {
            if (wOk&&cOK) {
                System.out.println("泡茶喝，茶喝完");
                this.warterOk = false;
                this.gap = 50 * 100;
            } else if (!wOk) {
                System.out.println("烧水失败，没有茶喝了");
            } else if (!cOK) {
                System.out.println("杯子洗不了，没有茶喝了");
            }
        }
    }

    //主任务
    public static void main(String[] args) {

        MainJob mainJob = new MainJob();

        new Thread(mainJob).start();

        HotWaterJob hotWaterJob = new HotWaterJob();

        WashJob washJob = new WashJob();

        ExecutorService executorPool = Executors.newFixedThreadPool(2);

        ListeningExecutorService listenPool = MoreExecutors.listeningDecorator(executorPool);

        //获取烧水future
        ListenableFuture<Boolean> hotWaterFuture = listenPool.submit(hotWaterJob);

        //烧水任务完成后添加回调和异常处理
        Futures.addCallback(hotWaterFuture, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                 if (aBoolean)
                     mainJob.warterOk=true;
                     System.out.println("水烧开了");
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("水烧不开啊");
            }
        });

        //获取洗茶壶future
        ListenableFuture<Boolean> washFuture = listenPool.submit(washJob);

        Futures.addCallback(washFuture, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (aBoolean)
                    mainJob.cupOk=true;
                    System.out.println("洗茶壶洗好了");
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("茶具被我摔坏了");
            }
        });




    }



}

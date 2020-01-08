package com.hank.netty.netty.io.fuctureExample;

import java.util.concurrent.*;

/**
 * @author lxt
 * @date 2020-01-07-13:10
 */
public class FuctureExaple {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        final ExecutorService service = Executors.newCachedThreadPool();
        Runnable task1 = new Runnable() {
            @Override
            public void run() {
                System.out.println(" i am task 1");
            }
        };

        Callable<Integer> task2 = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return new Integer(100);
            }
        };
        final Future<?> f1 = service.submit(task1);
        final Future<Integer> f2 = service.submit(task2);
        System.out.println(" task1 is completed?" + f1);
        System.out.println("task2 is completed?" + f2);

        while (f1.isDone()) {
            System.out.println("task1 completed.");
            break;

        }
        //waiting task2 completed
        do {
            System.out.println("wait for task2 completed");
        } while (!f2.isDone());

        final Integer integer = f2.get();
        System.out.println("result task2:  " + integer);
        service.shutdown();
    }
}

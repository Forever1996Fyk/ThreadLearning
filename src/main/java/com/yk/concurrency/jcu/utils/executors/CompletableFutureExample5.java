package com.yk.concurrency.jcu.utils.executors;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-11-02 20:21
 **/
public class CompletableFutureExample5 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

//        getNow();
//        complete();
        join();
        Thread.currentThread().join();
    }

    private static void join() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("==== I will be still process...");
            return "Hello";
        });

        String result = future.join();
        System.out.println(result);
    }

    /**
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void complete() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("==== I will be still process...");
            return "Hello";
        });

        boolean finished = future.complete("World");
        System.out.println(finished);
        System.out.println(future.get());
    }

    /**
     * getNow() 会直接返回一个其中的结果
     *
     * future.get()还是返回future中的结果
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void getNow() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            sleep(5);
            return "Hello";
        });

        String result = future.getNow("World");

        System.out.println(result);
        System.out.println(future.get());
    }

    private static void sleep(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
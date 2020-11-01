package com.yk.concurrency.jcu.utils.executors;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-11-01 21:26
 **/
public class CompletableFutureExample2 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
//        supplyAsync();

//        Future<?> future = runAsync();
//        future.get();

//        Future<Void> future = completed("Hello");
//        System.out.println(future.isDone());

//        Future<?> future = anyOf();
//        System.out.println(">>>>>" + future.get());

        allOff();
        Thread.currentThread().join();
    }

    /**
     * 执行所有的CompletableFuture
     */
    private static void allOff() {
        CompletableFuture.allOf(CompletableFuture.runAsync(() -> {
                    System.out.println("1 ==== Start");
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("1 ==== End");
                }).whenComplete((v, t) -> System.out.println("========Over========")),
                CompletableFuture.supplyAsync(() -> {
                    System.out.println("2 ==== Start");
                    try {
                        TimeUnit.SECONDS.sleep(4);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("2 ==== End");
                    return "Hello";
                }).whenComplete((v, t) -> System.out.println(v + "========Over========")));
    }

    /**
     * 执行全部的CompletableFuture, 并且返回其中任意一个future
     * @return
     */
    private static Future<?> anyOf() {
        return CompletableFuture.anyOf(CompletableFuture.runAsync(() -> {
            System.out.println("1 ==== Start");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("1 ==== End");
        }).whenComplete((v, t) -> System.out.println("========Over========")),
                CompletableFuture.supplyAsync(() -> {
                    System.out.println("2 ==== Start");
                    try {
                        TimeUnit.SECONDS.sleep(4);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("2 ==== End");
                    return "Hello";
                }).whenComplete((v, t) -> System.out.println(v + "========Over========")));
    }

    /**
     * 直接完成一个future任务
     * @param data
     * @return
     */
    private static Future<Void> completed(String data) {
        return CompletableFuture.completedFuture(data).thenAccept(System.out::println);
    }

    /**
     * 提交一个runnable, 并且异步运行
     * @return
     */
    private static Future<?> runAsync() {
       return CompletableFuture.runAsync(() -> {
            System.out.println("Obj ==== Start");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Obj ==== End");
        }).whenComplete((v, t) -> System.out.println("========Over========"));
    }

    /**
     * 提交任意一个supply类型, 并且异步运行
     */
    private static void supplyAsync() {
        CompletableFuture.supplyAsync(Object::new).thenAcceptAsync(obj -> {
            try {
                System.out.println("Obj ==== Start");
                TimeUnit.SECONDS.sleep(5);
                System.out.println("Obj ==== "+ obj);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).runAfterBoth(CompletableFuture.supplyAsync(() -> "Hello")
                .thenAcceptAsync(s -> {
                    try {
                        System.out.println("String ==== Start");
                        TimeUnit.SECONDS.sleep(3);
                        System.out.println("String ==== "+ s);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }), () -> System.out.println("====finished====="));
    };
}
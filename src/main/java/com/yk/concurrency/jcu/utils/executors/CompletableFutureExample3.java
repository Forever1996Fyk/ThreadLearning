package com.yk.concurrency.jcu.utils.executors;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-11-01 22:09
 **/
public class CompletableFutureExample3 {

    /**
     * 可以看出其中的规律: 下一个CompletableFuture, 处理的是上一个CompletableFuture的结果
     * @param args
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync((Supplier<String>) () -> {
//            throw new RuntimeException("not get the data");
//        })
//                .whenComplete((v, t) -> System.out.println("done"));

//                .whenCompleteAsync((v, t) -> {
//                    try {
//                        TimeUnit.SECONDS.sleep(2);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println("====Over====");
//                });

//                .thenApply(String::length);

//                .thenApplyAsync(s -> {
//                    try {
//                        System.out.println("=======");
//                        TimeUnit.SECONDS.sleep(2);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    return s.length();
//                });

//                .handleAsync((s, t) -> {
//                    Optional.of(t).ifPresent(e -> System.out.println("Error"));
//                    return s==null?0:s.length();
//                });

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello");
        future.thenAcceptAsync(System.out::println);

        System.out.println(future.get());

        Thread.currentThread().join();
    }
}
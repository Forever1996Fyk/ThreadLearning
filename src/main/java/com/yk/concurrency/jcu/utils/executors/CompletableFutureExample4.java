package com.yk.concurrency.jcu.utils.executors;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @program: ThreadLearning
 * @description: CompletableFuture方法组合
 * @author: YuKai Fan
 * @create: 2020-11-02 19:57
 **/
public class CompletableFutureExample4 {

    public static void main(String[] args) throws InterruptedException {
//        thenAcceptBoth();
//        acceptEither();
//        runAfterBoth();
//        runAfterEither();
//        combine();
        compose();
        Thread.currentThread().join();
    }

    /**
     * compose会把第一个future的结果, 作为第二个future的输入。
     *
     * 所以compose()是阻塞的方法, 必须先执行第一个future, 才会执行第二个future
     */
    private static void compose() {
        CompletableFuture.supplyAsync(() -> {
            System.out.println("start the compose1");
            sleep(5);
            System.out.println("end the compose1");
            return "compose-1";
        }).thenCompose(s -> CompletableFuture.supplyAsync(() -> {
            System.out.println("start the compose1");
            sleep(5);
            System.out.println("end the compose1");
            return s.length();
        })).thenAccept(System.out::println);
    }

    /**
     * combine可以同时执行两个CompletableFuture, 它也会返回这两个future的结果, 而且combine并不在意最后的结果类型
     *
     * 第一个future返回的是String, 第二个future返回的是int, 但是最终的结果返回的时boolean
     *
     * 并且返回最后的结果, 还可以进行处理, 但是thenAcceptBoth返回的是void 无法在处理,
     */
    private static void combine() {
        CompletableFuture.supplyAsync(() -> {
            System.out.println("start the combine1");
            sleep(5);
            System.out.println("end the combine1");
            return "combine-1";
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            System.out.println("start the combine2");
            sleep(3);
            System.out.println("end the combine2");
            return 100;
        }), (s, i) -> s.length() > i).whenComplete((v, t) -> System.out.println(v));
    }

    /**
     * runAfterBoth()可以同时执行两个CompletableFuture, 但是不会返回结果, 只要有一个future执行完成, 就立刻执行回调runnable方法
     */
    private static void runAfterEither() {
        CompletableFuture.supplyAsync(() -> {
            System.out.println("start the runAfterEither1");
            sleep(5);
            System.out.println("end the runAfterEither1");
            return "runAfterEither-1";
        }).runAfterEither(CompletableFuture.supplyAsync(() -> {
            System.out.println("start the runAfterEither2");
            sleep(3);
            System.out.println("end the runAfterEither2");
            return 100;
        }), () -> System.out.println("DONE"));
    }

    /**
     * runAfterBoth()可以同时执行两个CompletableFuture, 但是不会返回结果。
     */
    private static void runAfterBoth() {
        CompletableFuture.supplyAsync(() -> {
            System.out.println("start the runAfterBoth1");
            sleep(5);
            System.out.println("end the runAfterBoth1");
            return "acceptEither-1";
        }).runAfterBothAsync(CompletableFuture.supplyAsync(() -> {
            System.out.println("start the runAfterBoth2");
            sleep(3);
            System.out.println("end the runAfterBoth2");
            return 100;
        }), () -> System.out.println("DONE"));
    }

    /**
     * acceptEither()可以同时执行两个CompletableFuture, 但是只要其中一个future先执行完, 那个future才会输出
     * 这两个CompletableFuture返回类型必须一致
     */
    private static void acceptEither() {
        CompletableFuture.supplyAsync(() -> {
            System.out.println("start the acceptEither1");
            sleep(5);
            System.out.println("end the acceptEither1");
            return "acceptEither-1";
        }).acceptEither(CompletableFuture.supplyAsync(() -> {
            System.out.println("start the acceptEither2");
            sleep(5);
            System.out.println("end the acceptEither2");
            return "acceptEither-2";
        }), System.out::println);
    }

    /**
     * thenAcceptBoth可以同时执行两个CompletableFuture, 并且返回这个两个future的结果
     *
     * 这两个CompletableFuture返回类型可以不一致
     */
    private static void thenAcceptBoth() {
        CompletableFuture.supplyAsync(() -> {
            System.out.println("start the supplyAsync");
            sleep(5);
            System.out.println("end the supplyAsync");
            return "thenAcceptBoth";
        }).thenAcceptBoth(CompletableFuture.supplyAsync(() -> {
            System.out.println("start the thenAcceptBoth");
            sleep(5);
            System.out.println("end the thenAcceptBoth");
            return 100;
        }), (s, i) -> System.out.println(s + "--" + i));
    }

    private static void sleep(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
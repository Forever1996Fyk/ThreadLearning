package com.yk.concurrency.jcu.utils.executors;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @program: ThreadLearning
 * @description: invokeAll() invokeAny(), submit()
 * @author: YuKai Fan
 * @create: 2020-10-27 20:18
 **/
public class ExecutorServiceExample4 {
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
//        testInvokeAny();
//        testInvokeAnyTimeout();
//        testInvokeAll();
//        testInvokeAllTimeout();
//        testSubmitRunnable();
        testSubmitRunnableWithResult();
    }


    /**
     * Callable与Runnable最大的区别是, Callable有返回值, 而Runnable无返回值
     *
     * 当invokeAny()结果返回时, 其他的callable就会被取消。
     * 但是可能存在, 在结束的过程中, 正好有任务完成。所以有可能即使返回结果, 但是也有多个callable完成任务
     *
     * invokeAny()方法时会block, 是同步的方法
     * {@link ExecutorService#invokeAny(Collection)}
     */
    private static void testInvokeAny() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        List<Callable<Integer>> callableList = IntStream.range(0, 5).boxed().map(
                i -> (Callable<Integer>) () -> {
                    TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
                    System.out.println(Thread.currentThread().getName() + " : " + i);
                    return i;
                }
        ).collect(Collectors.toList());

        // invokeAny只返回单一的结果, 也就是, 提交的callable任务, 只会返回最先完成的任务返回值
        Integer value = executorService.invokeAny(callableList);
        System.out.println("===========finished===========");
        System.out.println(value);
    }

    /**
     * 在invokeAny(Collection)的基础上加了超时功能, 如果超过指定时间会抛出异常
     *
     * {@link ExecutorService#invokeAny(Collection, long, TimeUnit)} )}
     */
    private static void testInvokeAnyTimeout() throws ExecutionException, InterruptedException, TimeoutException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        List<Callable<Integer>> callableList = IntStream.range(0, 5).boxed().map(
                i -> (Callable<Integer>) () -> {
                    TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(20));
                    System.out.println(Thread.currentThread().getName() + " : " + i);
                    return i;
                }
        ).collect(Collectors.toList());

        // invokeAny只返回单一的结果, 也就是, 提交的callable任务, 只会返回最先完成的任务返回值
        Integer value = executorService.invokeAny(callableList, 3, TimeUnit.SECONDS);
        System.out.println("===========finished===========");
        System.out.println(value);
    }

    /**
     * invokeAll()也是blocked, 同步方法。它会执行完所有的callable的任务后, 返回所有的future结果。
     * 下面的代码分为三个阶段:
     * 1. 完成所有的callable任务
     * 2. 转换future集合
     * 3. 输出
     *
     * 这里面有一个明显的问题, 如果执行callable任务时, 其中一个方法很慢时, 也必须要等待所有任务执行完成, 才能执行下面的代码。
     *
     *
     * {@link ExecutorService#invokeAll(Collection)}
     * @throws InterruptedException
     */
    private static void testInvokeAll() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Callable<Integer>> callableList = IntStream.range(0, 5).boxed().map(
                i -> (Callable<Integer>) () -> {
                    TimeUnit.SECONDS.sleep(5);
                    System.out.println(Thread.currentThread().getName() + " : " + i);
                    return i;
                }
        ).collect(Collectors.toList());

        // invokeAny只返回单一的结果, 也就是, 提交的callable任务, 只会返回最先完成的任务返回值
        List<Future<Integer>> futures = executorService.invokeAll(callableList);
        futures.parallelStream().map(future -> {
            try {
                return future.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).forEach(System.out::println);
        System.out.println("==========finished========");
    }

    /**
     *
     * {@link ExecutorService#invokeAll(Collection, long, TimeUnit)}
     * @throws InterruptedException
     */
    private static void testInvokeAllTimeout() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Callable<Integer>> callableList = IntStream.range(0, 5).boxed().map(
                i -> (Callable<Integer>) () -> {
                    TimeUnit.SECONDS.sleep(20);
                    System.out.println(Thread.currentThread().getName() + " : " + i);
                    return i;
                }
        ).collect(Collectors.toList());

        // invokeAny只返回单一的结果, 也就是, 提交的callable任务, 只会返回最先完成的任务返回值
        List<Future<Integer>> futures = executorService.invokeAll(callableList, 1, TimeUnit.SECONDS);
        futures.parallelStream().map(future -> {
            try {
                return future.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).forEach(System.out::println);
        System.out.println("==========finished========");
    }

    /**
     * {@link ExecutorService#submit(Runnable)}
     */
    private static void testSubmitRunnable() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Future<?> future = executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Object o = future.get();
        System.out.println("Result: " + o);
    }

    /**
     * {@link ExecutorService#submit(Runnable, Object)}
     */
    private static void testSubmitRunnableWithResult() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        String result = "DONE";
        Future<String> future = executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, result);

        System.out.println("Result: " + future.get());
    }
}
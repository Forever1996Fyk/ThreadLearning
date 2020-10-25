package com.yk.concurrency.jcu.utils.executors;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-25 15:15
 **/
public class ExecutorsExample2 {

    /**
     * newWorkStealingPool()会用到所有cpu可用的进程数, 来执行任务。
     *
     *   return new ForkJoinPool
     *             (Runtime.getRuntime().availableProcessors(),
     *              ForkJoinPool.defaultForkJoinWorkerThreadFactory,
     *              null, true);
     *
     *   要注意的时ForkJoinPool有两个executor方法, 它们的参数不同一个是ForkJoinTask, 一个是Runnable。
     *   但是ForkJoinPool.executor(Runnable task)的源码中, 还是会判断是否是ForkJoinTask的类型, 如果是就直接执行任务, 如果不是就会把Runnable封装成ForkJoinTask类型
     *
     *   public void execute(Runnable task) {
     *         if (task == null)
     *             throw new NullPointerException();
     *         ForkJoinTask<?> job;
     *         if (task instanceof ForkJoinTask<?>) // avoid re-wrap
     *             job = (ForkJoinTask<?>) task;
     *         else
     *             job = new ForkJoinTask.RunnableExecuteAction(task);
     *         externalPush(job);
     *     }
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newWorkStealingPool();

        /**
         * 这是future设计模式
         */
        List<Callable<String>> callableList = IntStream.range(0, 20).boxed().map(i ->
                (Callable<String>) () -> {
                    System.out.println("Thread " + Thread.currentThread().getName());
                    sleep(2);
                    return "Task-" + i;
                }).collect(Collectors.toList());

        // 全部唤醒callable时会返回一个Future的集合, 也就是callable的返回值集合。 但是并不会立即得到结果, 直到执行完所有的任务才会真正的返回futures的值。
//        List<Future<String>> futures = executorService.invokeAll(callableList);
        executorService.invokeAll(callableList).stream().map(future -> {
            try {
                return future.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).forEach(System.out::println);
    }

    private static void sleep(long s) {
        try {
            TimeUnit.SECONDS.sleep(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
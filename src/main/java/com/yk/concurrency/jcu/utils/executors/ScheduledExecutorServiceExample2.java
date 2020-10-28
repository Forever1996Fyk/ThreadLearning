package com.yk.concurrency.jcu.utils.executors;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-28 21:45
 **/
public class ScheduledExecutorServiceExample2 {

    public static void main(String[] args) throws InterruptedException {
        /*ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);

        *//**
         * 等待某个任务执行完成后, 在延迟固定一段时间再去执行下一个任务
         *
         * {@link java.util.concurrent.ScheduledExecutorService#scheduleWithFixedDelay(Runnable, long, long, TimeUnit)}
         *//*
        executor.scheduleWithFixedDelay(() -> System.out.println("====" + System.currentTimeMillis()), 1, 2, TimeUnit.SECONDS);*/

//        testContinueExistingPeriodicTasksAfterShutdownPolicy();

        testExecuteExistingDelayedTasksAfterShutdownPolicy();
    }

    /**
     * 如果ScheduledThreadPoolExecutor.setContinueExistingPeriodicTasksAfterShutdownPolicy(true), 即使把线程池shutdown了, 但是任务还是会周期的执行
     *
     * 默认是false
     *
     * {@link ScheduledThreadPoolExecutor#setContinueExistingPeriodicTasksAfterShutdownPolicy(boolean)}
     * @throws InterruptedException
     */
    private static void testContinueExistingPeriodicTasksAfterShutdownPolicy() throws InterruptedException {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        System.out.println(executor.getContinueExistingPeriodicTasksAfterShutdownPolicy());
        executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(true);
        executor.scheduleAtFixedRate(() -> System.out.println("I am working.."), 1, 2, TimeUnit.SECONDS);
        TimeUnit.MILLISECONDS.sleep(1200);
        executor.shutdown();
        System.out.println("==over==");
    }

    /**
     * {@link ScheduledThreadPoolExecutor#setExecuteExistingDelayedTasksAfterShutdownPolicy(boolean)}
     * @throws InterruptedException
     */
    private static void testExecuteExistingDelayedTasksAfterShutdownPolicy() throws InterruptedException {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        System.out.println(executor.getExecuteExistingDelayedTasksAfterShutdownPolicy());
        executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        executor.scheduleWithFixedDelay(() -> System.out.println("I am working.."), 1, 2, TimeUnit.SECONDS);
        TimeUnit.MILLISECONDS.sleep(1200);
        executor.shutdown();
        System.out.println("==over==");
    }
}
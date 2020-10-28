package com.yk.concurrency.jcu.utils.executors;

import java.util.concurrent.*;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-28 21:07
 **/
public class ScheduledExecutorServiceExample1 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        /*
        // runnable
        ScheduledFuture<?> future = executor.schedule(() -> System.out.println("===== I will be invoked!"), 2, TimeUnit.SECONDS);
        System.out.println(future.cancel(true));
        */

        // callable
        /*ScheduledFuture<Integer> future = executor.schedule(() -> 2, 2, TimeUnit.SECONDS);
        System.out.println(future.get());*/

        /**
         * 定时周期任务, 1秒后执行一次, 之后每两秒执行一次
         *
         * 对于定时周期任务, 依然有一个问题。如果每两秒执行一次, 但是执行的任务需要花费5秒, 怎么办?
         * 一般有两种方案, 如果执行周期是2s, 执行时长是5s:
         *
         * 第一种方案: 完全符合执行周期, 也就是每2s都会启动一个任务 (Quartz, crontab)
         * 0 : 5
         * 2 : 5
         * 4 : 5
         *
         * 第二种方案: 当任务执行完成时, 从任务执行完成的时间开始周期执行(ScheduleExecutorService, JDK Timer)
         *
         * 0 : 5
         * 5 : 5
         * 10 : 5
         * 15 : 5
         *
         */
        ScheduledFuture<?> future = executor.scheduleAtFixedRate(() -> System.out.println("I am running " + System.currentTimeMillis()), 1, 2, TimeUnit.SECONDS);
    }
}
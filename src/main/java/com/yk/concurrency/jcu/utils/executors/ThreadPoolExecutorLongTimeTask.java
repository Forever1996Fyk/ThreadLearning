package com.yk.concurrency.jcu.utils.executors;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-25 11:41
 **/
public class ThreadPoolExecutorLongTimeTask {

    /**
     * 如果线程中的每个任务都花费很长的时间,  而又不想让线程就这样一直处于等待, 那么就可以为每一个线程任务设置守护线程。
     *
     * 当awaitTermination()达到等待时间时, main线程就不会处于等待状态了, 如果main线程结束了, 那么它的守护线程也就跟着结束了, 那整个线程池中的线程任务也就结束了。
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = new ThreadPoolExecutor(10, 20, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10), r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        }, new ThreadPoolExecutor.AbortPolicy());

        IntStream.range(0, 10).boxed().forEach(i -> {
            executorService.submit(() -> {
                while (true) {

                }
            });
        });

        executorService.shutdownNow();

        executorService.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("========start the sequence working==========");
    }
}
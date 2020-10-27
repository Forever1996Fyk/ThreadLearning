package com.yk.concurrency.jcu.utils.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-27 21:35
 **/
public class ExecutorServiceExample5 {
    public static void main(String[] args) {
        testAddQueue();
    }

    /**
     * 我们拿到这个任务队列直接往队列中加入任务, 会执行吗?
     *
     * 答案是不执行。
     *
     * 通过submit或者invoke, execute方法执行任务的时候, 不仅仅是接收到任务, 还会受到一个信号。这个信号会触发线程池提供线程来执行这个任务。
     *
     */
    private static void  testAddQueue() {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

        // 如果我们调用execute方法时, 就会触发线程池调用线程去执行任务, 此时直接往workQueue中添加任务, 才会去执行。但是直接添加任务, 是无法执行的
        // executorService.execute(() -> System.out.println("I will be process because of triggered the execute."));
        executorService.getQueue().add(() -> System.out.println("I am added directly into the queue"));
    }
}
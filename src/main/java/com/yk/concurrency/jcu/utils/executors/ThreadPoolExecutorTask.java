package com.yk.concurrency.jcu.utils.executors;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-25 10:52
 **/
public class ThreadPoolExecutorTask {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = new ThreadPoolExecutor(10, 20, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10), r -> {
            Thread thread = new Thread(r);
            return thread;
        }, new ThreadPoolExecutor.AbortPolicy());

        IntStream.range(0, 20).boxed().forEach(i ->
                executorService.execute(() -> {
                    try {
                        TimeUnit.SECONDS.sleep(20);
                        System.out.println(Thread.currentThread().getName() + " [" + i + "] " + "finished done.");
                    } catch (InterruptedException e) {
//                        e.printStackTrace();
                    }
                }));

        /**
         * shutdown()的作用会中断空闲的线程, 但是正在执行的线程会等待结束任务
         * shutdown()的步骤:
         * 1. 等待正在完成任务的线程结束
         * 2. 中断空闲的线程
         * 3. 所有空闲的线程退出
         *
         * 如果有20个线程, 10个在执行任务, 10个是空闲的; 如果执行shutdown()方法, 先会等待10个线程的任务执行完成, 然后中断10个空闲的线程, 最后20个空闲线程退出
         * 也是shutdown()方法时异步方法的原因(需要等待正在执行的线程完成)
         *
         * awaitTermination(1, TimeUnit.HOURS)表示等待线程池中的所有任务都完成之后才会去执行后面的任务, 如果任务超过1个小时, 就不会等待, 直接执行后面的任务。
         *
         * List<Runnable> shutdownNow() 这是有返回值的方法, 返回的是workQueue中的线程集合, 也就是还在等待的线程集合。
         * 如果有10个核心线程, queue的大小也是10
         *
         * 那么有10个线程正在执行任务, 有10个线程存储在blocking queue中
         * 此时调用shutdownNow(), 首先会返回blocking queue中的线程集合, 也就是10个线程集合; 然后会执行完10个正在执行的任务。
         *
         * shutdownNow()的步骤:
         * 1. 先排出正在等待队列的线程,
         * 2. 再中断所有的线程。
         *
         *
         */
//        executorService.shutdown();
        List<Runnable> runnableList = null;
        try {
            runnableList = executorService.shutdownNow();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        executorService.awaitTermination(1, TimeUnit.HOURS);
        System.out.println("=============over=============");
        System.out.println(runnableList);
        System.out.println(runnableList.size());
    }
}
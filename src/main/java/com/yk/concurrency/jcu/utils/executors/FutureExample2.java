package com.yk.concurrency.jcu.utils.executors;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-27 22:11
 **/
public class FutureExample2 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        testIsDone();
//        testCancel();
        testCancel2();
    }

    /**
     *
     * 在三种情况下, isDone()才会返回true.
     * 1. future正常结束, 这里的正常结束指的是future中的结果被正确的拿出, 也就是future.get()
     * 2. future中任务出现异常
     * 3. future被canceled
     *
     * {@link Future#isDone()}
     */
    private static void testIsDone() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        Future<Integer> future = executorService.submit(() -> {
            throw new RuntimeException();
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return 10;
        });

        try {
            Integer result = future.get();
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("is done " + future.isDone());
        }
    }

    /**
     * future.cancel会尝试取消一个任务,
     * 1. 如果这个任务已经完成, 那么就会取消失败, 返回false
     * 2. 这个任务已经被取消过了, 那么也会取消失败, 返回false
     *
     *
     * {@link Future#cancel(boolean)}
     */
    private static void testCancel() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        /*Future<Integer> future = executorService.submit(() -> 10);
        System.out.println(future.get());
        System.out.println(future.cancel(true));*/

        /*Future<Integer> future = executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        });

        TimeUnit.MILLISECONDS.sleep(10);
        System.out.println(future.cancel(true));
        System.out.println(future.cancel(true));*/

        AtomicBoolean running = new AtomicBoolean(true);
        Future<Integer> future = executorService.submit(() -> {
            while (running.get()) {

            }
            return 10;
        });
        TimeUnit.MILLISECONDS.sleep(10);
        System.out.println(future.cancel(true));
        System.out.println(future.isDone());
        System.out.println(future.isCancelled());

    }

    /**
     * 一般情况下, cancel方法时跟interrupt一起使用.
     *
     * Future被cancel后, 就无法在拿出结果了。在调用future.get()会抛出异常
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void testCancel2() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            }
        });
        AtomicBoolean running = new AtomicBoolean(true);
        Future<Integer> future = executorService.submit(() -> {
            // 但是如果这个循环中某个方法还是执行的非常长, 那么就要采用守护线程的方式, 让整个线程都死掉
            while (running.get()) {

            }

            // 如果访问时间特别长, 那么想要cancel, 要判断线程是否被打断, 如果打断了, 就直接退出
//            while (!Thread.interrupted()) {
//            }

            System.out.println("11111");
            return 10;
        });
        TimeUnit.MILLISECONDS.sleep(10);
        System.out.println(future.cancel(true));
        System.out.println(future.isDone());
        System.out.println(future.isCancelled());

    }
}
package com.yk.concurrency.jcu.utils.executors;

import java.util.concurrent.*;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-27 21:43
 **/
public class FutureExample1 {

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
//        testGet();
        testGetTimeout();
    }

    /**
     * 我们看到Future的get()会抛出InterruptedException, 那么异常指的是谁?
     * 首先不是future, 因为future不是一个线程, 没有中断的方法, 而且future无法中断。
     *
     * 要注意的是future.get()方法时main线程执行的, 所以这个中断异常, 指的是调用get()方法的线程中断所抛出的异常
     *
     * {@link Future#get()}
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void testGet() throws ExecutionException, InterruptedException {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        Future<Integer> future = executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        });

        System.out.println("==========I Will be execute other work==========");

        Thread thread = Thread.currentThread();
        new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            thread.interrupt();
        }).start();

        Integer result = future.get();
        System.out.println(result);
    }

    /**
     * 对于get方法的超时, 即使get()时抛出超时异常, 但是future中的线程还是会继续执行任务。
     *
     * {@link Future#get(long, TimeUnit)}
     *
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    private static void testGetTimeout() throws ExecutionException, InterruptedException, TimeoutException {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        Future<Integer> future = executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
                System.out.println("==============");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        });

        System.out.println("==========I Will be execute other work==========");
        Integer result = future.get(5, TimeUnit.SECONDS);
        System.out.println(result);
    }
}
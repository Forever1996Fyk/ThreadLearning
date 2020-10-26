package com.yk.concurrency.jcu.utils.executors;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-25 20:56
 **/
public class ExecutorServiceExample2 {

    public static void main(String[] args) throws InterruptedException {
//        AbortPolicyTest();
//        DiscardPolicyTest();
//        CallerRunsPolicyTest();
        DiscardOldestPolicyTest();
    }

    /**
     * AbortPolicy拒绝策略, 当线程池达到最大线程数, 且队列也满时, 再提交任务就会抛出异常
     * @throws InterruptedException
     */
    private static void AbortPolicyTest() throws InterruptedException {
        ExecutorService executorService = new ThreadPoolExecutor(1, 2, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1), r -> {
            Thread thread = new Thread(r);
            return thread;
        }, new ThreadPoolExecutor.AbortPolicy());

        for (int i = 0; i < 3; i++) {
            executorService.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        TimeUnit.SECONDS.sleep(1);
        executorService.execute(() -> System.out.println("x"));
    }

    /**
     * DiscardPolicy拒绝策略, 当线程池达到最大线程数, 且队列也满时, 再提交任务就会直接忽略不会执行
     * @throws InterruptedException
     */
    private static void DiscardPolicyTest() throws InterruptedException {
        ExecutorService executorService = new ThreadPoolExecutor(1, 2, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1), r -> {
            Thread thread = new Thread(r);
            return thread;
        }, new ThreadPoolExecutor.DiscardPolicy());

        for (int i = 0; i < 3; i++) {
            executorService.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        TimeUnit.SECONDS.sleep(1);
        executorService.execute(() -> System.out.println("x"));
        System.out.println("==============");
    }

    /**
     * CallerRunsPolicy拒绝策略, 当线程池达到最大线程数, 且队列也满时, 再提交任务会交给提交任务的线程(我们这里是main线程)执行, 不会交给线程池中的线程。
     * @throws InterruptedException
     */
    private static void CallerRunsPolicyTest() throws InterruptedException {
        ExecutorService executorService = new ThreadPoolExecutor(1, 2, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1), r -> {
            Thread thread = new Thread(r);
            return thread;
        }, new ThreadPoolExecutor.CallerRunsPolicy());

        for (int i = 0; i < 3; i++) {
            executorService.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        TimeUnit.SECONDS.sleep(1);
        executorService.execute(() -> {
            System.out.println("x");
            System.out.println(Thread.currentThread().getName());
        });
        System.out.println("==============");
    }

    /**
     * DiscardOldestPolicy拒绝策略, 当线程池达到最大线程数, 且队列也满时, 再提交任务会将queue中最先进入的任务移除掉, 将新提交的任务加入队列中
     * @throws InterruptedException
     */
    private static void DiscardOldestPolicyTest() throws InterruptedException {
        ExecutorService executorService = new ThreadPoolExecutor(1, 2, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1), r -> {
            Thread thread = new Thread(r);
            return thread;
        }, new ThreadPoolExecutor.DiscardOldestPolicy());

        for (int i = 0; i < 3; i++) {
            executorService.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(5);
                    System.out.println("I come from lambda.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        TimeUnit.SECONDS.sleep(1);
        executorService.execute(() -> {
            System.out.println("x");
            System.out.println(Thread.currentThread().getName());
        });
        System.out.println("==============");
    }


}
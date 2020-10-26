package com.yk.concurrency.jcu.utils.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-26 20:15
 **/
public class ExecutorServiceExample3 {

    public static void main(String[] args) throws InterruptedException {
//        test();
//        testAllowCoreThreadTimeout();
//        testRemove();
        testPrestartCoreThread();
    }

    /**
     * newFixedThreadPool()中设置线程数时, 虽然核心线程一直保持固定(因为是ThreadPoolExecutor), 但是只有当提交任务时, 才算有活动的线程。
     * @throws InterruptedException
     */
    private static void test() throws InterruptedException {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        System.out.println(executorService.getActiveCount());
        executorService.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        TimeUnit.MICROSECONDS.sleep(20);
        System.out.println(executorService.getActiveCount());
    }

    /**
     *
     * allowCoreThreadTimeOut(true)是允许线程池中的线程可以被回收时, 才会派上用场
     * 当keepLiveTime为0时, allowCoreThreadTimeOut(true)会抛出异常Core threads must have nonzero keep alive times。
     * 所以如果要允许ThreadPoolExecutor中的线程可以回收的话, 必须要有keepLiveTime
     */
    private static void testAllowCoreThreadTimeout() {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        executorService.setKeepAliveTime(10L, TimeUnit.SECONDS);
        executorService.allowCoreThreadTimeOut(true);
        IntStream.range(0, 5).boxed().forEach(i -> executorService.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
    }

    /**
     * ExecutorService.remove(Runnable) 是移除queue中的任务
     * @throws InterruptedException
     */
    private static void testRemove() throws InterruptedException {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        IntStream.range(0, 2).boxed().forEach(i -> executorService.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
                System.out.println("======= I am finished");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));

        TimeUnit.MILLISECONDS.sleep(20);
        Runnable runnable = () -> System.out.println("I will never be executed");
        executorService.execute(runnable);
        TimeUnit.MILLISECONDS.sleep(20);
        executorService.remove(runnable);
    }

    /**
     * executorService.prestartCoreThread();当ThreadPoolExecutor存在空闲线程时, 会启动一个没有任务的线程, 并且会返回一个boolean类型的。
     */
    private static void testPrestartCoreThread() {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        System.out.println(executorService.getActiveCount());

        System.out.println(executorService.prestartCoreThread());
        System.out.println(executorService.getActiveCount());

        System.out.println(executorService.prestartCoreThread());
        System.out.println(executorService.getActiveCount());
        executorService.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        executorService.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println(executorService.prestartCoreThread());
        System.out.println(executorService.getActiveCount());
    }

}
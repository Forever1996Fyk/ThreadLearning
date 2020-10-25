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
 * @create: 2020-10-25 12:49
 **/
public class ExecutorExample1 {
    public static void main(String[] args) throws InterruptedException {
//        userCachedThreadPool();
//        useFixedSizePool();
        useSinglePool();
    }

    /**
     * newSingleThreadExecutor()与一个Thread的区别:
     *
     * 1. Thread执行完一个任务之后, 周期就结束了; 但是newSingleThreadExecutor中的线程会一直存在
     * 2. Thread无法将任务提交到queue中; 但是newSingleThreadExecutor可以。
     *
     * return new FinalizableDelegatedExecutorService
     *             (new ThreadPoolExecutor(1, 1,
     *                                     0L, TimeUnit.MILLISECONDS,
     *                                     new LinkedBlockingQueue<Runnable>()));
     *
     *
     * 通过上面的源码发现, newSingleThreadExecutor实际上是FinalizableDelegatedExecutorService的封装,
     * 而FinalizableDelegatedExecutorService继承了DelegatedExecutorService, DelegatedExecutorService是一个内部包装类,
     * 仅仅只暴露ExecutorService的实现方法, 无法调用ThreadPoolExecutor中的所有方法。
     *
     * 所以newSingleThreadExecutor无法强制转换成ThreadPoolExecutor, 也无法用ThreadPoolExecutor中的一些方法。
     */
    private static void useSinglePool() throws InterruptedException {
        //等价于Executors.newFixedThreadPool(1);
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(() -> System.out.println("============="));

        IntStream.range(0, 100).boxed().forEach(i -> executorService.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " [" + i + "] ");
        }));

        TimeUnit.SECONDS.sleep(1);
    }

    /**
     * new ThreadPoolExecutor(nThreads, nThreads,
     *                                       0L, TimeUnit.MILLISECONDS,
     *                                       new LinkedBlockingQueue<Runnable>());
     *
     * newFixedThreadPool(int nThreads) 这个构成参数 即使corePoolSize, 也是maximumPoolSize。
     * 这样每次都只会最多有10个线程去处理任务
     */
    private static void useFixedSizePool() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        System.out.println(((ThreadPoolExecutor)executorService).getActiveCount());

        executorService.execute(() -> System.out.println("============="));
        System.out.println(((ThreadPoolExecutor)executorService).getActiveCount());

        IntStream.range(0, 100).boxed().forEach(i -> executorService.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " [" + i + "] ");
        }));

        TimeUnit.SECONDS.sleep(1);
        System.out.println(((ThreadPoolExecutor)executorService).getActiveCount());
    }

    /**
     *  These pools will typically improve the performance
     *  of programs that execute many short-lived asynchronous tasks.
     *
     *  return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
     *                                       60L, TimeUnit.SECONDS,
     *                                       new SynchronousQueue<Runnable>());
     *
     *  通过阅读源码发现newCachedThreadPool返回的时corePoolSize=0, maximumPoolSize=Integer.MAX_VALUE的线程池。
     *  而且block queue用的是SynchronizedQueue, 意思是队列中每次只能插入一条数据。
     *  这就导致了, 这个queue只能存储一个任务, 所以不会把任务暂存下来。
     *  当我们提交100任务时, 由于不会存储任务, 如果发现有线程是空闲的, 就直接去处理;
     *  在等待keepLiveTime 60s之后, 发现这些线程的个数大于corePoolSize, 最后就会全部回收。
     *
     *  换句话说, 如果有100个任务, userCachedThreadPool()就会用100个线程去处理任务。
     *
     *  所以newCachedThreadPool()适用于执行一些非常短的异步任务, 如果执行时间很长, 那么线程就会越来越多, 最后可能就出现stack OOM。
     */
    private static void userCachedThreadPool() throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        System.out.println(((ThreadPoolExecutor)executorService).getActiveCount());

        executorService.execute(() -> System.out.println("============="));
        System.out.println(((ThreadPoolExecutor)executorService).getActiveCount());

        IntStream.range(0, 100).boxed().forEach(i -> executorService.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " [" + i + "] ");
        }));

        TimeUnit.SECONDS.sleep(1);
        System.out.println(((ThreadPoolExecutor)executorService).getActiveCount());
    }
}
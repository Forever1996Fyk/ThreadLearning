package com.yk.concurrency.jcu.utils.executors;

import java.util.concurrent.*;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-25 09:34
 **/
public class ThreadPoolExecutorBuild {

    public static void main(String[] args) {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) buildThreadPoolExecutor();

        int activeCount = -1;
        int queueSize = -1;

        while (true) {
            if (activeCount != executorService.getActiveCount() || queueSize != executorService.getQueue().size()) {
                System.out.println(executorService.getActiveCount());
                System.out.println(executorService.getCorePoolSize());
                System.out.println(executorService.getQueue().size());
                System.out.println(executorService.getMaximumPoolSize());
                activeCount = executorService.getActiveCount();
                queueSize = executorService.getQueue().size();

                System.out.println("=========================");
            }
        }
    }

    /**
     *
     * 一定到理解ThreadPoolSize中的这7个参数, 很重要。在ThreadPoolSize中已经有很详细的解释。
     *
     * 这里的关系要搞清楚:
     * corePoolSize是核心线程大小, 线程池中固定保持多少个线程, 即使线程时空闲的, 换句话说不管是正在执行的线程, 还是空闲的线程只能保持在corePoolSize的数量
     * workQueue任务队列, 这里要注意, 当任务提交时, 会先存到这个队列, 然后会去判断线程池是否还有空闲的线程, 如果没有就会一直在队列中等待。直到达到队列大小。
     * maximumPoolSize最大线程大小, 是线程池中允许最大的线程数量是多少。当workQueue中的任务数量已经达到队列大小时, 就会扩充线程池的线程数量到最大线程数
     *
     * 提交的任务先会存到workQueue中, 然后才会去调用线程执行, 当核心线程大小不够时, 提交的任务就会一直在workQueue中等待执行;
     * 但是如果提交任务过多, 超过了workQueue的大小, 那么线程池数量就会扩充到最大线程数; 而如果此时任务还是不断提交, 超过了最大线程数, 那么就会执行拒绝策略。
     *
     * 按照上面的解释, 如果corePoolSize = 1, maximumPoolSize=2, workQueue.size = 1; 如果任务执行时间过长, 导致线程还没来得及回收,那么最多只能提交3个任务.
     * 当提交第4个任务时就会执行拒绝策略
     *
     *
     * int corePoolSize, 核心线程大小
     * int maximumPoolSize, 最大线程大小
     * long keepAliveTime,
     * TimeUnit unit,
     * BlockingQueue<Runnable> workQueue, 任务队列
     * ThreadFactory threadFactory, 创建线程工厂
     * RejectedExecutionHandler handler 拒绝策略处理
     */
    private static ExecutorService buildThreadPoolExecutor() {
        ExecutorService executorService = new ThreadPoolExecutor(1, 2, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1), r -> {
                    Thread thread = new Thread(r);
                    return thread;
                }, new ThreadPoolExecutor.AbortPolicy());

        System.out.println("=========The ThreadPoolExecutor create done.");

        executorService.execute(() -> sleep(100));
        executorService.execute(() -> sleep(100));
        executorService.execute(() -> sleep(100));

        return executorService;
    }

    private static void sleep(long s) {
        try {
            System.out.println("* " + Thread.currentThread().getName() + " *");
            TimeUnit.SECONDS.sleep(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
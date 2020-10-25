package com.yk.concurrency.jcu.utils.executors;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-25 17:14
 **/
public class ExecutorServiceExample1 {

    /**
     * The demo for class {@link java.util.concurrent.ExecutorService}
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
//        isShutDown();
//        isTerminated();
//        executeRunnableError();
        executeRunnableTask();
    }

    /**
     * 当调用shutdown()方法之后, 虽然shutdown()方法是异步的, 但是之后就无法在用线程执行任务。
     * {@link ExecutorService#isShutdown()}
     */
    private static void isShutDown() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println(executorService.isShutdown());
        executorService.shutdown();
        System.out.println(executorService.isShutdown());
        executorService.execute(() -> {
            System.out.println("I will be executed after shutdown");
        });
    }

    /**
     * ExecutorService.isTerminated() 是线程池是否终结
     * ThreadPoolExecutor.isTerminating() 是线程池是否在终结的过程中
     * <p>
     * {@link ExecutorService#isTerminated()}
     * {@link ThreadPoolExecutor#isTerminating()}
     */
    private static void isTerminated() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        executorService.shutdown();
        System.out.println(executorService.isShutdown());
        System.out.println(executorService.isTerminated());
        System.out.println(((ThreadPoolExecutor) executorService).isTerminating());
    }

    /**
     * ExecutorService.isTerminated() 是线程池是否终结
     * ThreadPoolExecutor.isTerminating() 是线程池是否在终结的过程中
     * <p>
     * {@link ExecutorService#isTerminated()}
     * {@link ThreadPoolExecutor#isTerminating()}
     */
    private static void executeRunnableError() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10, new MyThreadFactory());
        IntStream.range(0, 10).boxed().forEach(i -> executorService.execute(() -> System.out.println(1 / 0)));
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.MINUTES);
        System.out.println("======================");
    }

    /**
     * 如果需求是, 在执行任务时出现异常时, 需要修改比如, 数据库的状态。
     */
    private static void executeRunnableTask() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        IntStream.range(0, 10).boxed().forEach(i ->
                executorService.execute(
                        new MyTask(i) {
                            @Override
                            protected void doInit() {

                            }

                            @Override
                            protected void doExecute() {
                                if (i % 3 == 0) {
                                    int temp = i / 0;
                                }
                            }

                            @Override
                            protected void done() {
                                System.out.println("The on: " + i + " successfully, update status to DONE");
                            }

                            @Override
                            protected void error(Throwable cause) {
                                System.out.println("The no:" + i + " failed, update status to ERROR");
                            }
                        }));
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.MINUTES);
        System.out.println("======================");
    }

    private abstract static class MyTask implements Runnable {

        private final int no;

        private MyTask(int no) {
            this.no = no;
        }

        @Override
        public void run() {
            try {
                this.doInit();
                this.doExecute();
                this.done();
            } catch (Throwable cause) {
                this.error(cause);
            }
        }


        protected abstract void doInit();

        protected abstract void doExecute();

        protected abstract void done();

        protected abstract void error(Throwable cause);
    }

    /**
     * 这种处理异常的方式只能对某个线程处理某个任务出错后, 进行日志的记录或者打印
     */
    private static class MyThreadFactory implements ThreadFactory {
        private final static AtomicInteger seq = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("My-Thread-" + seq.getAndIncrement());
            thread.setUncaughtExceptionHandler((t, cause) -> {
                System.out.println("The Thread " + t.getName() + " execute failed.");
                cause.printStackTrace();
                System.out.println("================================");
            });
            return thread;
        }
    }

}
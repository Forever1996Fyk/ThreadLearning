package com.yk.concurrency.base.chapter13;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @program: ThreadLearning
 * @description: 自定义简单线程池
 * @author: YuKai Fan
 * @create: 2020-10-06 21:23
 **/
public class SimpleThreadPool extends Thread {
    private int size;

    private int queueSize;

    private int min;

    private int max;

    private int active;

    //默认大小
    private final static int DEFAULT_SIZE = 10;

    //任务队列默认大小
    private final static int DEFAULT_TASK_QUEUE_SIZE = 2000;

    private static volatile int seq = 0;

    private final static String THREAD_PREFIX = "SIMPLE_THREAD_POOL-";

    private final static ThreadGroup GROUP = new ThreadGroup("Pool_Group");

    //任务队列
    private final static LinkedList<Runnable> TASK_QUEUE = new LinkedList<>();

    private final static List<WorkerTask> THREAD_QUEUE = new ArrayList<>();

    private final DiscardPolicy discardPolicy;

    private final static DiscardPolicy DEFAULT_DISCARD_POLICY = () -> {
        throw new DiscardException("Discard This Task.");
    };

    private volatile boolean destroy = false;

    public SimpleThreadPool() {
        this(4, 8, 12, DEFAULT_TASK_QUEUE_SIZE, DEFAULT_DISCARD_POLICY);
    }

    /**
     * 在new SimpleThreadPool时会加载init()方法, 创建size数量的线程
     */
    public SimpleThreadPool(int min, int active, int max, int queueSize, DiscardPolicy discardPolicy) {
        this.min = min;
        this.active = active;
        this.max = max;
        this.queueSize = queueSize;
        this.discardPolicy = discardPolicy;
        init();
    }

    private void init() {
        // 先判断线程数min是否足够
        for (int i = 0; i < min; i++) {
            createWorkTask();
        }
        this.size = min;
        this.start();
    }

    public void submit(Runnable runnable) {
        if (destroy) {
            throw new IllegalStateException("这个线程池已经销毁了, 不允许提交任务");
        }
        synchronized (TASK_QUEUE) {
            if (TASK_QUEUE.size() > queueSize) {
                discardPolicy.discard();
            }
            TASK_QUEUE.addLast(runnable);
            TASK_QUEUE.notifyAll();
        }
    }

    /**
     * 线程池的线程: 用于维护控制线程池中的扩充因子, 例如最大最小线程数等。
     */
    @Override
    public void run() {
        while (!destroy) {
            System.out.printf("Pool#Min:%d, Active:%d, Max:%d, Current:%d,QueueSize:%d\n",
                    this.min, this.active, this.max, this.size, TASK_QUEUE.size());
            try {
                Thread.sleep(5_000L);
                // 如果任务队列的任务数已经大于活跃的线程数, 并且线程池大小小于活跃的线程数, 那么就扩充线程池的大小。
                if (TASK_QUEUE.size() > active && size < active) {
                    for (int i = size; i < active; i++) {
                        createWorkTask();
                    }
                    System.out.println("The Pool incremented to active");
                    size = active;
                } else if (TASK_QUEUE.size() > max && size < max) {// 如果任务队列的任务数已经大于最大的线程数, 并且线程池大小小于最大的线程数, 那么就扩充线程池的大小到最大线程数。
                    for (int i = size; i < max; i++) {
                        createWorkTask();
                    }
                    System.out.println("The Pool incremented to max");
                    size = max;
                }
                synchronized (THREAD_QUEUE) {
                    // 如果任务队列已经空了, 那就占用TASK_QUEUE的锁, 降低线程池的线程数量
                    if (TASK_QUEUE.isEmpty() && size > active) {
                        System.out.println("=======Reduce=======");
                        int releaseSize = size - active;
                        for (Iterator<WorkerTask> it = THREAD_QUEUE.iterator(); it.hasNext(); ) {
                            if (releaseSize <= 0) {
                                break;
                            }

                            WorkerTask task = it.next();
                            task.close();
                            task.interrupt();
                            it.remove();
                            releaseSize--;
                        }
                        size = active;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void createWorkTask() {
        WorkerTask task = new WorkerTask(GROUP, THREAD_PREFIX + (seq++));
        task.start();
        THREAD_QUEUE.add(task);
    }

    public void shutdown() throws InterruptedException {
        while (!TASK_QUEUE.isEmpty()) {
            Thread.sleep(50);
        }
        synchronized (THREAD_QUEUE) {
            int initVal = THREAD_QUEUE.size();
            while (initVal > 0) {
                for (WorkerTask task : THREAD_QUEUE) {
                    if (task.getTaskState() == TaskState.BLOCKED) {
                        task.interrupt();
                        task.close();
                        initVal--;
                    } else {
                        Thread.sleep(10);
                    }
                }
            }
        }
        this.destroy = true;
        System.out.println("线程池已经关闭了");
    }

    public int getSize() {
        return size;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public boolean isDestroy() {
        return this.destroy;
    }

    private enum TaskState {
        FREE, RUNNING, BLOCKED, DEAD
    }

    public static class DiscardException extends RuntimeException {
        public DiscardException(String message) {
            super(message);
        }
    }

    public interface DiscardPolicy {
        void discard() throws DiscardException;
    }

    private static class WorkerTask extends Thread {
        private volatile TaskState taskState = TaskState.FREE;

        public WorkerTask(ThreadGroup threadGroup, String name) {
            super(threadGroup, name);
        }

        public TaskState getTaskState() {
            return this.taskState;
        }

        @Override
        public void run() {
            OUTER:
            while (this.taskState != TaskState.DEAD) {
                Runnable runnable;
                synchronized (TASK_QUEUE) {
                    while (TASK_QUEUE.isEmpty()) {
                        try {
                            taskState = TaskState.BLOCKED;
                            TASK_QUEUE.wait();
                        } catch (InterruptedException e) {
                            System.out.println("Close.");
                            break OUTER;
                        }
                    }
                    //队列先进先出, 这里remove first
                    runnable = TASK_QUEUE.removeFirst();
                }

                if (runnable != null) {
                    taskState = TaskState.RUNNING;
                    runnable.run();
                    taskState = TaskState.FREE;
                }
            }
        }

        public void close() {
            this.taskState = TaskState.DEAD;
        }
    }

    public static void main(String[] args) throws InterruptedException {
//        SimpleThreadPool threadPool = new SimpleThreadPool(6, 10, SimpleThreadPool.DEFAULT_DISCARD_POLICY);
        SimpleThreadPool threadPool = new SimpleThreadPool();
        for (int i = 0; i < 40; i++) {
            int finalI = i;
            threadPool.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("当前任务线程 " + finalI + "服务" + Thread.currentThread() + " 开始。");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("当前任务线程 " + finalI + "服务" + Thread.currentThread() + " 结束。");
                }
            });
        }

        Thread.sleep(10_000);
        threadPool.shutdown();
//        threadPool.submit(() -> System.out.println("再次提交任务"));
    }
}
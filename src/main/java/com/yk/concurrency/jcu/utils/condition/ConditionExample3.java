package com.yk.concurrency.jcu.utils.condition;


import com.google.common.collect.Lists;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * @program: ThreadLearning
 * @description: 利用Condition实现生产者消费者
 * @author: YuKai Fan
 * @create: 2020-10-24 10:23
 **/
public class ConditionExample3 {
    private final static ReentrantLock lock = new ReentrantLock();

    private final static Condition PRODUCE_COND = lock.newCondition();

    private final static Condition CONSUME_COND = lock.newCondition();

    /**
     * 存放时间戳池
     */
    private final static LinkedList<Long> TIMESTAMP_POOL = Lists.newLinkedList();

    /**
     * 时间戳池最大存储量
     */
    private final static int MAX_CAPACITY = 100;

    public static void main(String[] args) {
        IntStream.range(0, 6).boxed().forEach(ConditionExample3::beginProduce);
        IntStream.range(0, 13).boxed().forEach(ConditionExample3::beginConsume);

        /**
         * 这里之前犯了一些错误, 我本想使用main线程来监控生产者, 消费者在执行过程中的线程情况, 所以用到了ReentrantLock中的getWaitQueueLength, hasWaiters方法
         * 但是抛出了IllegalMonitorStateException异常, 后面阅读注释发现, 这些方法必须要在持有当前condition的锁的线程中, 才能调用。
         * 也就是说, 必须要在lock.lock()的方法中才能调用
         */
        /*while (true) {
            sleep(5);
            System.out.println("============================");
            System.out.println("PRODUCE_COND.getWaitQueueLength>>" + lock.getWaitQueueLength(PRODUCE_COND));
            System.out.println("CONSUME_COND.getWaitQueueLength>>" + lock.getWaitQueueLength(CONSUME_COND));
            System.out.println("PRODUCE_COND.hasWaiters>>" + lock.hasWaiters(PRODUCE_COND));
            System.out.println("CONSUME_COND.hasWaiters>>" + lock.hasWaiters(CONSUME_COND));
        }*/
    }

    private static void beginProduce(int i) {
        new Thread(() -> {
            while (true) {
                produce();
                sleep(2);
            }
        }, "P-" + i).start();
    }

    private static void beginConsume(int i) {
        new Thread(() -> {
            while (true) {
                consume();
                sleep(3);
            }
        }, "C-" + i).start();
    }

    public static void produce() {
        try {
            lock.lock();
            while (TIMESTAMP_POOL.size() >= MAX_CAPACITY) {
                PRODUCE_COND.await();
            }
            System.out.println("PRODUCE_COND.getWaitQueueLength>>" + lock.getWaitQueueLength(PRODUCE_COND));
            long value = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + "-p- " + value);
            TIMESTAMP_POOL.addLast(value);

            CONSUME_COND.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void consume() {
        try {
            lock.lock();
            while (TIMESTAMP_POOL.isEmpty()) {
                CONSUME_COND.await();
            }
            System.out.println("CONSUME_COND.getWaitQueueLength>>" + lock.getWaitQueueLength(CONSUME_COND));
            Long value = TIMESTAMP_POOL.removeFirst();
            System.out.println(Thread.currentThread().getName() + "-c- " + value);
            PRODUCE_COND.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private static void sleep(long s) {
        try {
            TimeUnit.SECONDS.sleep(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
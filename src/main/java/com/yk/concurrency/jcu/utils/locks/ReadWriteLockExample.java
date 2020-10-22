package com.yk.concurrency.jcu.utils.locks;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @program: ThreadLearning
 * @description: 读写锁
 * @author: YuKai Fan
 * @create: 2020-10-22 21:47
 **/
public class ReadWriteLockExample {
    private final static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);

    private final static Lock readLock = readWriteLock.readLock();
    private final static Lock writeLock = readWriteLock.writeLock();

    private static final List<Long> data = Lists.newArrayList();

    /**
     * 实现读写锁的基本要求为:
     *
     * 可以同时读,
     * 不可同时写,
     * 当有线程写入时, 不可读,
     * 当有线程读时, 不可写入,
     *
     * 所以如果使用一般的锁, 对于有很多读线程的操作时, 效率是不高的。这时候就需要读写锁
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(ReadWriteLockExample::write);
        thread1.start();

        TimeUnit.SECONDS.sleep(1);

        Thread thread2 = new Thread(ReadWriteLockExample::write);
        thread2.start();

    }

    public static void write() {
        try {
            writeLock.lock();
            data.add(System.currentTimeMillis());
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }

    public static void read() {
        try {
            readLock.lock();
            data.forEach(System.out::println);
            TimeUnit.SECONDS.sleep(5);
            System.out.println(Thread.currentThread().getName() + " =======================");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
    }
}
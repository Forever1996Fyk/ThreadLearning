package com.yk.concurrency.jcu.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-15 22:25
 **/
public class CompareAndSetLock {
    private final AtomicInteger value = new AtomicInteger(0);

    private Thread lockedThread;

    public void tryLock() throws GetLockException{
        boolean b = value.compareAndSet(0, 1);
        if (!b) {
            throw new GetLockException("Get tge Lok failed");
        } else {
            //设置排它锁, 在释放锁时, 判断线程是否是当前线程, 如果不加这个, 那么其他线程在unlock()时, 又会把这个锁释放, 导致其他线程又可以获取锁。
            lockedThread = Thread.currentThread();
        }
    }

    public void unlock() {
        if (0 == value.get()) {
            return;
        }
        if (lockedThread == Thread.currentThread()) {
            value.compareAndSet(1, 0);
        }
    }
}
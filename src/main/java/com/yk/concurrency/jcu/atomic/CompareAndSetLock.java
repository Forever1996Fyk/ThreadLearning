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

    public void tryLock() throws GetLockException{
        boolean b = value.compareAndSet(0, 1);
        if (!b) {
            throw new GetLockException("Get tge Lok failed");
        }
    }

    public void unlock() {
        if (0 == value.get()) {
            return;
        }
        value.compareAndSet(1, 0);
    }
}
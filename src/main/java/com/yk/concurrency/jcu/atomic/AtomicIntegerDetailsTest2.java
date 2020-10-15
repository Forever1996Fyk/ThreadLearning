package com.yk.concurrency.jcu.atomic;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-15 22:19
 **/
public class AtomicIntegerDetailsTest2 {
    private final static CompareAndSetLock LOCK = new CompareAndSetLock();

    public static void main(String[] args) {
        for (int  i = 0; i < 2; i++) {
            new Thread(() -> {
                try {
                    doSomething2();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private static void doSomething() throws InterruptedException {
        synchronized (AtomicIntegerDetailsTest2.class) {
            System.out.println(Thread.currentThread().getName() + "get the lock");
            Thread.sleep(100000);
        }
    }

    /**
     * 自定义tryLock显示锁, 利用AtomicInteger的compareAndSet()方法
     * @throws InterruptedException
     * @throws GetLockException
     */
    private static void doSomething2() throws InterruptedException, GetLockException {
        try {
            LOCK.tryLock();
            System.out.println(Thread.currentThread().getName() + "get the lock");
            Thread.sleep(100000);
        } finally {
            LOCK.unlock();
        }
    }
}
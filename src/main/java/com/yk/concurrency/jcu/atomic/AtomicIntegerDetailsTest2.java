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
        for (int  i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    doSomething2();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    /**
     * 这种方式, 一个线程抢到锁, 其他线程就处于blocked状态
     * @throws InterruptedException
     */
    private static void doSomething() throws InterruptedException {
        synchronized (AtomicIntegerDetailsTest2.class) {
            System.out.println(Thread.currentThread().getName() + "get the lock");
            Thread.sleep(100000);
        }
    }

    /**
     * 自定义tryLock显示锁, 利用AtomicInteger的compareAndSet()方法
     *
     * 如果一个线程抢到了锁, 那么其他线程就会直接抛出异常, 而不是blocked
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
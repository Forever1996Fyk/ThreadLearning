package com.yk.concurrency.base.chapter7;

/**
 * @program: ThreadLearning
 * @description: 对于锁的概念需要注意的是,
 *
 *               synchronized(obj)的意思是对这个obj上锁, 只有获取到这把这个对象锁的钥匙才能够执行里面的程序, 而怎么样获取这把锁。这就需要多线程去竞争钥匙。
 *
 *               哪个线程获取到了钥匙, 才会去执行里面的代码。
 *
 *               所以如果有两个线程去执行 synchronized(obj) 的代码, 那么这两个线程是竞争关系, 只有当某个线程获取到锁才能执行自己的代码。有可能Thread0获取到, 也有可能Thread1获取到
 *
 * @author: YuKai Fan
 * @create: 2020-09-28 20:17
 **/
public class SynchronizedTest {
    private final static Object LOCK = new Object();

    public static void main(String[] args) {
        Runnable runnable = () -> {
            /**
             * 同步代码块, 要注意的是, synchronized(obj), 是给obj上锁, 多个线程去抢obj的这把锁。
             */
            synchronized (LOCK) {
                try {
                    Thread.sleep(200_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        Thread t3 = new Thread(runnable);

        t1.start();
        t2.start();
        t3.start();
    }
}
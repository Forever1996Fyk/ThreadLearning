package com.yk.concurrency.base.chapter7;

/**
 * @program: ThreadLearning
 * @description: static方法锁
 * @author: YuKai Fan
 * @create: 2020-09-28 21:09
 **/
public class SynchronizedStatic {
    static {
        /**
         * 静态代码块加的锁时class锁, 对当前的class加锁。
         *
         * 所以调用SynchronizedStatic的方法时, 先回初始化这个静态代码块, 然后发现加了锁, 于是就是由第一个线程先执行静态代码块中的逻辑。
         *
         * 执行完成后, 在会去调用对应的方法, 如果是m1, m2, 那又会对this加锁, 但是由于m3没有加锁, 所以几乎会同时调用m3方法。
         */
        synchronized (SynchronizedStatic.class) {
            System.out.println("m1 " + Thread.currentThread().getName());

            try {
                Thread.sleep(10_000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized static void m1() {
        System.out.println("m1 " + Thread.currentThread().getName());

        try {
            Thread.sleep(10_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void m2() {
        System.out.println("m2 " + Thread.currentThread().getName());

        try {
            Thread.sleep(10_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void m3() {
        System.out.println("m3 " + Thread.currentThread().getName());

        try {
            Thread.sleep(10_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
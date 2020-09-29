package com.yk.concurrency.base.chapter9;

import java.util.stream.Stream;

/**
 * @program: ThreadLearning
 * @description: wait和sleep的区别
 * @author: YuKai Fan
 * @create: 2020-09-29 21:04
 **/
public class DiffWaitAndSleep {
    private final static Object lock = new Object();


    /**
     * 1. sleep() 是一个 Thread的方法, 而wait() 是Object的方法
     *
     * 2. sleep() 不会释放锁, 但是 wait() 会释放锁并且加入到对象的等待队列
     *
     * 3. 使用sleep() 不需要使用synchronized, 而 wait() 必须要使用synchronized
     *
     * 4. sleep() 不需要被唤醒, 但是 wait() 必须要被唤醒( 除 wait(10) 设置时间也可以自定唤醒)
     */

    public static void main(String[] args) {
//        m1();
//        m2();

        /*Stream.of("T1", "T2").forEach(name -> new Thread(name) {
            @Override
            public void run() {
                m3();
            }
        }.start());*/

        Stream.of("T1", "T2").forEach(name -> new Thread(name) {
            @Override
            public void run() {
                m4();
            }
        }.start());
    }

    /**
     * 使用 sleep(), 并不需要加锁, 直接可以调用
     */
    public static void m1() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 在调用wait()方法时, 如果不加锁, 直接使用会抛出异常(java.lang.IllegalMonitorStateException)
     */
    public static void m2() {
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 在加锁中 调用sleep()方法, 一个线程进入方法会睡眠一段时间, 另一个线程开会抢到锁。导致线程延迟执行代码
     */
    public static void m3() {
        synchronized (lock) {
            try {
                System.out.println("The Thread " + Thread.currentThread().getName() + " enter");
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 在加锁中使用 wait()方法, 多个线程会几乎同时进入方法, 然后调用wait() 方法时, 程序会处于等待状态。
     *
     * 要注意, 这里多个线程几乎同时进入方法原因是:
     *
     * 这里当线程A抢到锁, 进入代码块, 打印输出A, 调用wait()后就会使得线程A处于等待状态, 并且释放了锁;
     *
     * 那么此时线程B就会抢到这个锁, 并且进入代码块, 打印输出B 调用wait(), 此时 线程B也会进入等待状态, 并且释放锁, 如果没有其他线程唤醒的话, 这两个线程就会一直等待。
     */
    public static void m4() {
        synchronized (lock) {
            try {
                System.out.println("The Thread " + Thread.currentThread().getName() + " enter");
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
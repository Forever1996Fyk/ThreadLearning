package com.yk.concurrency.jcu.utils.locks;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-21 23:35
 **/
public class ReentrantLockExample {

    // 显示锁
    private static final ReentrantLock lock = new ReentrantLock();

    /**
     * lock(), lockInterruptibly(), tryLock()的区别在于
     *
     * lock()不会阻止线程的打断, 如果一个线程在等待状态时调用interrupt()方法时, 是无法打断的
     *
     * lockInterruptibly(),线程是可以被中断的。 当线程被中断时, 会抛出被中断的异常
     *
     * tryLock()会去尝试的获取锁, 他会有一个boolean的返回值, 如果获取不到锁, 就直接返回false
     */

    public static void main(String[] args) throws InterruptedException {

        //lock()
//        IntStream.range(0, 2).forEach(i -> new Thread(() -> {
////                needLock();
//            needLockBySync();
//        }).start());

        // lockInterruptibly()
        /*Thread thread1 = new Thread(() -> testUnInterruptibly());
        thread1.start();
        TimeUnit.SECONDS.sleep(1);

        Thread thread2 = new Thread(() -> testUnInterruptibly());
        thread2.start();
        TimeUnit.SECONDS.sleep(1);

        thread2.interrupt();
        System.out.println("===============");*/


        // tryLock()
        /*Thread thread1 = new Thread(() -> testTryLock());
        thread1.start();

        TimeUnit.SECONDS.sleep(1);

        Thread thread2 = new Thread(() -> testTryLock());
        thread2.start();*/

        Thread thread1 = new Thread(() -> testUnInterruptibly());
        thread1.start();
        TimeUnit.SECONDS.sleep(1);

        Thread thread2 = new Thread(() -> testUnInterruptibly());
        thread2.start();
        TimeUnit.SECONDS.sleep(1);

        /**
         * int getQueueLength() 获取等待队列中线程的数量
         *
         * boolean hasQueuedThreads() 判断有没有线程在等待队列汇总
         *
         * boolean hasQueuedThread(Thread thread) 判断某个线程是否在等待队列汇总
         *
         * boolean isLocked() 判断其他线程是否hold
         */
        Optional.of(lock.getQueueLength()).ifPresent(System.out::println);
        Optional.of(lock.hasQueuedThreads()).ifPresent(System.out::println);
        Optional.of(lock.hasQueuedThread(thread2)).ifPresent(System.out::println);
        Optional.of(lock.isLocked()).ifPresent(System.out::println);
        Optional.of(lock.getHoldCount()).ifPresent(System.out::println);

    }

    public static void testTryLock() {
        boolean b = lock.tryLock();
        if (b) {
            try {
                Optional.of("The thread-" + Thread.currentThread().getName() + " get lock and  will do working.").ifPresent(System.out::println);
                while (true) {

                }
            } finally {
                lock.unlock();
            }
        } else {
            Optional.of("The thread-" + Thread.currentThread().getName() + " not get lock.").ifPresent(System.out::println);
        }
    }

    public static void testUnInterruptibly() {
        try {
            lock.lockInterruptibly();
            Optional.of("The thread-" + Thread.currentThread().getName() + " get lock and  will do working.").ifPresent(System.out::println);
            while (true) {

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void needLock() {
        try {
            lock.lock();
            Optional.of("The thread-" + Thread.currentThread().getName() + " get lock and  will do working.").ifPresent(System.out::println);
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void needLockBySync() {
        synchronized (ReentrantLockExample.class) {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
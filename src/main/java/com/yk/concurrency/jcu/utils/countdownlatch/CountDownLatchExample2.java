package com.yk.concurrency.jcu.utils.countdownlatch;

import java.util.concurrent.CountDownLatch;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-19 22:42
 **/
public class CountDownLatchExample2 {
    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        /**
         * 在执行线程1时, 遇到latch.await(), 就会暂停执行, 然后线程2启动就会进行异步操作, 直到线程2执行完成, latch.countDown()
         * 线程1才会继续执行
         *
         * latch.await()允许一系列的线程处于等待状态, 知道latch.countDown()为0
         */
        new Thread(() -> {
            System.out.println("Do some initial working.");
            try {
                Thread.sleep(1000);
                latch.await();

                System.out.println("Do other working...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                latch.await();
                System.out.println("release.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            System.out.println("async prepare for some data.");
            try {
                Thread.sleep(2000);
                System.out.println("data prepare for done.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        }).start();

        Thread.currentThread().join();

    }
}
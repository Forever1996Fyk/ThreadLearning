package com.yk.concurrency.jcu.utils.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-19 22:51
 **/
public class CountDownLatchExample3 {
    /**
     * 1. CountDownLatch参数不能为0且小于0, 如果为0那么await()方法就失效了, 为负数那么就会抛出运行时异常IllegalArgumentException
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        final Thread mainThread = Thread.currentThread();

        new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 如果main线程直接中断, 就会导致latch直接唤醒, 但是也会抛出异常
//            mainThread.interrupt();
            latch.countDown();
        }).start();

//        latch.await();

        // 设置等待时间, 1s后如果还没执行完成, 就不在等待线程的结果
        latch.await(1000, TimeUnit.MILLISECONDS);
        System.out.println("==================");
        latch.countDown();
    }
}
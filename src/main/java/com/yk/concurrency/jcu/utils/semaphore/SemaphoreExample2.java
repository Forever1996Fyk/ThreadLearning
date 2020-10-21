package com.yk.concurrency.jcu.utils.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-21 22:54
 **/
public class SemaphoreExample2 {

    /**
     * 当得到不可用的连接时的策略:
     * 1. 设置一个超时时间, 如果超过1000MS还没有获取到可用连接时, 就抛出异常
     * 2. 直接blocked, 直到有可用的连接时, 在获取
     * 3. 直接抛弃这个请求 discard
     * 4. 直接抛出异常
     * 5. 如果获取不到, 会注册一个回调方法, 当连接可用时, 再去调用回调方法
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        final Semaphore semaphore = new Semaphore(2);

        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " in");
                try {
                    semaphore.acquire(1);
                    System.out.println(Thread.currentThread().getName() + " get the semaphore");
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release(1);
                }
                System.out.println(Thread.currentThread().getName() + " out");
            }).start();

            while (true) {
                System.out.println("AP->" + semaphore.availablePermits());
                System.out.println("QL->" + semaphore.getQueueLength());
                System.out.println("===========================");
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }
}
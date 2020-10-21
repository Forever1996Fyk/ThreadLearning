package com.yk.concurrency.jcu.utils.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @program: ThreadLearning
 * @description: Semaphore是信号量的意思, 它可以作为锁来使用
 * @author: YuKai Fan
 * @create: 2020-10-21 22:35
 **/
public class SemaphoreExample1 {

    /**
     * new Semaphore(1);  permits 的意思是许可证。
     *
     * 每一个调用Semaphore都会blocked, 当线程调用acquire()方法时, 可以获取许可证, 每获取一个许可证就会减一, 直到permits为0时, 那么其他线程就无法访问, 也就是被锁住了
     * 当线程调用release()方法时, permits许可证就会恢复一个, 也就是释放了锁。
     *
     * 根据上面的解释, 当许可证permits设为1时, 就可以自定义lock
     *
     *
     * @param args
     */
    public static void main(String[] args) {
        final SemaphoreLock lock = new SemaphoreLock();

        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " is Running");
                    lock.lock();
                    System.out.println(Thread.currentThread().getName() + " get the $SemaphoreLock");
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }

                System.out.println(Thread.currentThread().getName() + " release.");

            }).start();
        }
    }

    static class SemaphoreLock {
        private final Semaphore semaphore = new Semaphore(1);

        public void lock() throws InterruptedException {
            // 申请一个许可证
            semaphore.acquire();
        }

        public void unlock() {
            // 释放许可证
            semaphore.release();
        }
    }
}
package com.yk.concurrency.jcu.utils.cyclicbarrier;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @program: ThreadLearning
 * @description: 利用CountDownLatch实现CyclicBarrier, 每执行完一个线程, 就进行通知
 * @author: YuKai Fan
 * @create: 2020-10-20 22:16
 **/
public class CyclicBarrierExample3 {

    static class MyCountDownLatch extends CountDownLatch {
        private final Runnable runnable;

        //这里的Runnable 就是模拟 CyclicBarrier的形式, 用于通知线程
        public MyCountDownLatch(int count, Runnable runnable) {
            super(count);
            this.runnable = runnable;
        }

        @Override
        public void countDown() {
            super.countDown();
            if (getCount() == 0) {
                this.runnable.run();
            }
        }
    }

    /**
     * 看起来好像是实现了CyclicBarrier的功能, 但是实际上是不一样的。
     *
     * CyclicBarrier本质是多个线程之前需要协同工作的, 而CountDownLatch是多个线程之间是没有联系的。
     *
     * 所以CountDownLatch 与 CyclicBarrier的区别:
     *
     * 最明显的区别是: CountDownLatch是不能reset的, 也就是不能重复利用(当countDown为0时, 就结束了), 而CyclicBarrier是可以reset的, 重复利用。
     * 最主要的区别是: CountDownLatch多个线程之间互不关心, 而CyclicBarrier必须要等到同一个共同点,采取执行某个动作
     *
     * 也就是说CyclicBarrier只有当所有线程都await()之后(因为每次await(), parties都会减一, 知道parties为0), 每个线程才会分别去执行对应的逻辑
     *
     * 通过分析堆栈信息:
     *
     * @param args
     */
    public static void main(String[] args) {
        final MyCountDownLatch myCountDownLatch = new MyCountDownLatch(2, new Runnable() {
            @Override
            public void run() {
                System.out.println("All of work finish done.");
            }
        });

        new Thread() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                myCountDownLatch.countDown();
                System.out.println(Thread.currentThread().getName() + " finished.");
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                myCountDownLatch.countDown();
                System.out.println(Thread.currentThread().getName() + " finished.");
            }
        }.start();
    }
}
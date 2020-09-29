package com.yk.concurrency.base.chapter9;

import java.util.stream.Stream;

/**
 * @program: ThreadLearning
 * @description: 生产者消费者版本3
 * @author: YuKai Fan
 * @create: 2020-09-28 21:56
 **/
public class ProduceConsumerVersion3 {
    private int i = 0;

    final private Object lock = new Object();

    // 表示判断生产者是否生产了数据. false 表示没有生产数据
    private volatile boolean isProduced = false;

    public void produce() {
        synchronized (lock) {

            // isProduced = true 表示 已经生成了, 那么生产者就需要 等待 消费者消费。
            // 这里使用 while 表示 所有的生产者 只要有一个生产了数据, 所有的生产者都会进入这个循环 并且处于等待状态
            while (isProduced) {
                try {
                    /**
                     * 这里一定要注意的是, wait(), notify()是 Object提供的方法
                     */
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            i++;
            System.out.println("P->" + i);

            // notifyAll() 唤醒所有等待锁的线程, 然后再次同时竞争抢锁
            lock.notifyAll();
            isProduced = true;
        }
    }

    public void consume() {
        synchronized (lock) {
            // isProduced = true, 表示生产者已经生产数据了, 所以 唤醒 消费者去消费数据, 并且 isProduced = false, 表示已经消费完了, 需要生产者继续生产
            while (!isProduced) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("C->" + i);
            lock.notifyAll();
            isProduced = false;
        }
    }

    public static void main(String[] args) {
        ProduceConsumerVersion3 pc3 = new ProduceConsumerVersion3();
        Stream.of("P1", "P2").forEach(n -> {
            new Thread(n) {
                @Override
                public void run() {
                    while (true) {
                        pc3.produce();
                    }
                }
            }.start();
        });

        Stream.of("C1", "C2").forEach(n -> {
            new Thread(n) {
                @Override
                public void run() {
                    while (true) {
                        pc3.consume();
                    }
                }
            }.start();
        });
    }
}
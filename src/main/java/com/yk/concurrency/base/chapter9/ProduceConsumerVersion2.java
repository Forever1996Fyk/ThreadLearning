package com.yk.concurrency.base.chapter9;

import java.util.stream.Stream;

/**
 * @program: ThreadLearning
 * @description: 生产者消费者版本2 改进
 *
 *               在这个版本中依然是有问题的, 在多生产者, 多消费者的环境中就会产生问题。
 *
 *               如果有两个生产者, 两个消费者就会产生问题。因为 wait(), notify()并不会指定某一个对应的线程去操作。
 *
 *               这就会导致所有的线程都处在wait的状态, 也不会产生死锁, 但是程序也不会运行。
 * @author: YuKai Fan
 * @create: 2020-09-28 21:56
 **/
public class ProduceConsumerVersion2 {
    private int i = 0;

    final private Object lock = new Object();

    // 表示判断生产者是否生产了数据. false 表示没有生产数据
    private volatile boolean isProduced = false;

    public void produce() {
        synchronized (lock) {

            // isProduced = true 表示 已经生成了, 那么生产者就需要 等待 消费者消费
            if (isProduced) {
                try {
                    /**
                     * 这里一定要注意的是, wait(), notify()是 Object提供的方法
                     */
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else { // isProduced = false 表示 消费者已经消费了数据, 需要 唤醒 生产者生产数据, 并且生产完数据后, 需要把isProduced = true, 表示已经生产完了, 需要消费者继续去消费
                i++;
                System.out.println("P->" + i);

                lock.notify();
                isProduced = true;
            }
        }
    }

    public void consume() {
        synchronized (lock) {
            // isProduced = true, 表示生产者已经生产数据了, 所以 唤醒 消费者去消费数据, 并且 isProduced = false, 表示已经消费完了, 需要生产者继续生产
            if (isProduced) {
                System.out.println("C->" + i);
                lock.notify();
                isProduced = false;
            } else {// isProduced = false, 表示生产者没有生成数据, 所以让消费者 等待 生产者生产数据
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ProduceConsumerVersion2 pc2 = new ProduceConsumerVersion2();
        Stream.of("P1", "P2").forEach(n -> {
            new Thread(n) {
                @Override
                public void run() {
                    while (true) {
                        pc2.produce();
                    }
                }
            }.start();
        });

        Stream.of("C1", "C2").forEach(n -> {
            new Thread(n){
                @Override
                public void run() {
                    while (true) {
                        pc2.consume();
                    }
                }
            }.start();
        });
    }
}
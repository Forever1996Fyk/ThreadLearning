package com.yk.concurrency.base.chapter9;

/**
 * @program: ThreadLearning
 * @description: 生产者, 消费者 版本1。
 * @author: YuKai Fan
 * @create: 2020-09-28 21:50
 **/
public class ProduceConsumerVersion1 {

    private int i = 1;

    private final Object lock = new Object();

    /**
     * 生产者, 写入数据
     */
    private void produce() {
        synchronized (lock) {
            System.out.println("P->" + (i++));
        }
    }

    /**
     * 消费者 读出数据
     */
    private void consume() {
        synchronized (lock) {
            System.out.println("C->" + i);
        }
    }

    public static void main(String[] args) {
        ProduceConsumerVersion1 pc1 = new ProduceConsumerVersion1();

        /**
         * 下面的情况会出现, Produce会不断的生成新的数据, 但是Consume并不会立马去读新的数据, 只有Consume线程抢到锁才会去读数据。
         *
         * 这就导致了原来的数据没有读到, 新的数据又被覆盖了。
         *
         * 产生这个情况的原因是, 这两个线程之间没有通讯, Produce线程生成数据, 没有通知Consume线程去读取。
         */
        new Thread("Produce") {
            @Override
            public void run() {
                while (true) {
                    pc1.produce();
                }
            }
        }.start();

        new Thread("Consume"){
            @Override
            public void run() {
                while (true) {
                    pc1.consume();
                }
            }
        }.start();

    }
}
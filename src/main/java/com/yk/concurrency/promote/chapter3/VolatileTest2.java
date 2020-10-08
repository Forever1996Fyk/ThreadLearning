package com.yk.concurrency.promote.chapter3;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-08 21:21
 **/
public class VolatileTest2 {
    /**
     * 如果不加volatile, reader就会进入死循环, 不会打印。
     * update线程修改 INIT_VALUE 之后, reader线程并没有感知到 INIT_VALUE 的变化, 所以localValue仍然为0
     *
     * 这部分内容就涉及到Java内存模型, 以及CPU相关问题
     *
     * 对于CPU内存而言, 分为主内存, 高速缓存。程序加载过程后, 数据会被加载到主内存和缓存。
     * 在多线程的环境下, 每个线程都只会在第一次在主内存中获取数据, 之后就会在缓存中拿数据。
     * 而对数据修改时, 会在缓存中修改数据后, 刷新到主内存中去
     *
     * 但是ADDER-1线程为什么一直不会执行呢?
     *
     * 这就是因为Java内存的优化导致的。
     * Java内存在判断一段程序在线程中没有任何写操作时, JVM就会认为这段程序不会去主内存拿数据, 而去缓存中拿数据, 而线程ADDER-1在缓存中的数据没有任何变化,就导致了INIT_VALUE一直不会变化
     *
     */
    private static int INIT_VALUE = 0;

    private final static int MAX_LIMIT = 50;

    public static void main(String[] args) {
        new Thread(() -> {
            while (INIT_VALUE < MAX_LIMIT) {
                System.out.println("T1 -> " + (++INIT_VALUE));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "ADDER-1").start();

        new Thread(() -> {
            while (INIT_VALUE < MAX_LIMIT) {
                System.out.println("T2 -> " + (++INIT_VALUE));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "ADDER-2").start();
    }
}
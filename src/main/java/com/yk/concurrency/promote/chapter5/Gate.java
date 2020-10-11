package com.yk.concurrency.promote.chapter5;


/**
 * @program: ThreadLearning
 * @description: 单线程执行模式, 有一个门, 始终只能一个人通过, 每次通过都需要记录这个人的信息。
 *
 *               但是这种加锁方式, 直接让程序串行化。每次进行读操作时, 也只能读一个人的信息。这样效率很低。所以可以采用读写分离锁的方式。
 *               读写分裂锁需要考虑四种情况, 因为是多个线程同时操作, 所以肯定要分别对数据考虑读写情况。
 *
 *               Y: 串行化, N: 并行化
 *               ----------------------
 *               +     | read |  write
 *               +---------------------
 *                read |  N   |  Y
 *                write|  Y   |  Y
 *
 *                所以在多个线程处理时,只有所有线程都对资源进行读取时, 才会对资源并行化处理
 * @author: YuKai Fan
 * @create: 2020-10-10 17:37
 **/
public class Gate {
    private int counter = 0;
    private String name = "Nobody";
    private String address = "Nowhere";

    /**
     * 临界值, 而且都是共享资源。所以在多线程环境下, 就会产生竞争。也就是线程安全问题
     * @param name
     * @param address
     */
    public synchronized void pass(String name, String address) {
        this.counter++;
        this.name = name;
        this.address = address;
        verify();
    }

    private void verify() {
        if (this.name.charAt(0) != this.address.charAt(0)) {
            System.out.println("*********BROKEN**********" + toString());
        }
    }

    @Override
    public synchronized String toString() {
        return "No." + counter + ":" + name + "," + address;
    }
}
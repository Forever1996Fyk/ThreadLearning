package com.yk.concurrency.base.chapter4;

/**
 * @program: ThreadLearning
 * @description: 守护线程
 * @author: YuKai Fan
 * @create: 2020-09-24 20:13
 **/
public class DaemonThread {

    /**
     * 在建立网络连接时: Client A <------------> Server B , A与B进行长连接, 在长连接中,A需要不断的向B发送心跳包, 来确认服务是否还正常
     *
     * 在连接创建好了之后, 需要维护心跳, 而这个心跳跟业务是没有关系的, 跟连接发送报文信息也没有关系, 心跳只是一个维护的作用。
     *
     * 如果长连接的线程断开了, 那么这个发送心跳的线程也就不需要存在了, 这时候就需要一个守护线程
     *
     * 创建连接之后的线程, 在开启一个DaemonThread(health check)守护线程。用这个守护线程来进行心跳检测。
     *
     * 如果不设置守护线程, 那么即使建立连接操作的线程(主线程)已经死了, 但是这个守护线程依然可以存在进行心跳检测, 就会造成内存的损耗(因为连接都不存在了, 还要心跳干嘛)
     *
     * 所以守护线程, 可以理解为守护主线程的一个线程, 一旦主线程结束了, 不管这个线程是不是active, 都直接结束。
     *
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName() + " running...");
                    Thread.sleep(10000);
                    System.out.println(Thread.currentThread().getName() + " done...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };// new

        // runnable -> running
        // runnable -> dead
        // runnable -> blocked
        t.setDaemon(true);
        t.start();

        Thread.sleep(5_000); // JDK1.7以后
        System.out.println(Thread.currentThread().getName());

        /**
         * 即使在主线程结束的情况下, 应用依然在执行, 这是由于还存在active的线程。那么JVM怎么知道存在active的线程呢? 就是通过ThreadGroup
         *
         * 但是在active的线程中设置setDaemon(true)的情况下, 只要Main线程执行结束了, 整个应用就直接退出了。
         */
    }
}
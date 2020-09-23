package com.yk.concurrency.base.chapter2;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-09-23 20:29
 **/
public class BankVersion2 {

    public static void main(String[] args) {
        /**
         * 使用Runnable接口, 不管每次定义多少个线程, 每次创建业务的实例只需要一个。
         *
         * 不想Bank类中, 需要创建多个TicketWindow业务实例
         */
        final TicketWindowRunnable ticketWindowRunnable = new TicketWindowRunnable();

        /**
         * 在调用线程时, 必须要传入Runnable接口或者重写Thread类中的run()方法才能够调用线程, 否则Thread不会调用任何东西
         *
         */
        Thread windowThread1 = new Thread(ticketWindowRunnable, "一号窗口");
        Thread windowThread2 = new Thread(ticketWindowRunnable, "二号窗口");
        Thread windowThread3 = new Thread(ticketWindowRunnable, "三号窗口");

        windowThread1.start();
        windowThread2.start();
        windowThread3.start();
    }
}
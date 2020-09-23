package com.yk.concurrency.base.chapter2;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-09-23 20:27
 **/
public class TicketWindowRunnable implements Runnable {
    private int index = 1;

    private final static int MAX = 50;

    public void run() {
        while (index <= MAX) {
            System.out.println(Thread.currentThread() + " 的号码是:" + (index++));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
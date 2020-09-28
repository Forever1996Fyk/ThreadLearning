package com.yk.concurrency.base.chapter7;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-09-28 21:02
 **/
public class SynchronizedThis {

    public static void main(String[] args) {
        ThisLock thisLock = new ThisLock();
        new Thread("T1") {
            @Override
            public void run() {
                thisLock.m1();
            }
        }.start();

        new Thread("T2") {
            @Override
            public void run() {
                thisLock.m2();
            }
        }.start();

    }
}

class ThisLock {
    /**
     * 当m1, m2都加synchronized时, 会发现两个线程调用m1,m2 明显有抢锁的 时间差距,
     *
     * 但是如果只有一个方法指定synchronized时, 那么几乎会同时调用m1,m2。
     *
     * 这就说明了, synchronized 锁的时同一个对象
     */
    public synchronized void m1() {
        try {
            System.out.println(Thread.currentThread().getName());
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void m2() {
        try {
            System.out.println(Thread.currentThread().getName());
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
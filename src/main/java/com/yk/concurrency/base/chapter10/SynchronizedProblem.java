package com.yk.concurrency.base.chapter10;

/**
 * @program: ThreadLearning
 * @description: Synchronized问题
 * @author: YuKai Fan
 * @create: 2020-10-05 21:30
 **/
public class SynchronizedProblem {


    public static void main(String[] args) throws InterruptedException {
        new Thread() {
            @Override
            public void run() {
                System.out.println("t1");
                SynchronizedProblem.run();
            }
        }.start();

        Thread.sleep(1000);

        Thread t2 = new Thread() {
            @Override
            public void run() {
                System.out.println("t2");
                SynchronizedProblem.run();
            }
        };

        t2.start();
        Thread.sleep(2000);
        t2.interrupt();
        System.out.println(t2.isInterrupted());

    }

    private synchronized static void run() {
        System.out.println(Thread.currentThread().getName());
        while (true) {
        }
    }
}
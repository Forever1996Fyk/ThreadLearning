package com.yk.concurrency.base.chapter9;

import java.util.concurrent.TimeUnit;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-27 16:46
 **/
public class Test {
    private static final Object object = new Object();

    private static volatile boolean flag = true;

    private static void waitTest() throws InterruptedException {
        synchronized (object) {
            System.out.println("Wait Test....");
            if (flag) {
                System.out.println("怎么说1");
                object.wait();
                System.out.println("怎么说2");
            }
            object.notify();
            flag = false;
        }
    }

    private static void notifyTest() throws InterruptedException {
        synchronized (object) {
            System.out.println("Notify Test....");
            if (!flag) {
                object.wait();
            }
            object.notify();
            flag = true;
        }
    }
    public static void main(String[] args) throws InterruptedException {
        new Thread() {
            @Override
            public void run() {
                try {
                    waitTest();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        TimeUnit.SECONDS.sleep(1);

        new Thread() {
            @Override
            public void run() {
                try {
                    notifyTest();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
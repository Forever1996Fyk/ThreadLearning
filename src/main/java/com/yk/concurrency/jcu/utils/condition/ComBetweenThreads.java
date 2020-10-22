package com.yk.concurrency.jcu.utils.condition;

import java.util.concurrent.TimeUnit;

/**
 * @program: ThreadLearning
 * @description: 线程间的交流
 * @author: YuKai Fan
 * @create: 2020-10-22 22:48
 **/
public class ComBetweenThreads {

    private static int data = 0;

    private static boolean noUse = true;

    private final static Object object = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            while (true) {
                build();
            }
        }).start();

        new Thread(() -> {
            while (true) {
                use();
            }
        }).start();

    }

    private static void sleep(long s) {
        try {
            TimeUnit.SECONDS.sleep(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static void build() {
        synchronized (object) {
            while (noUse) {
                try {
                    object.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            data++;
            sleep(1);
            System.out.println("P: " + data);
            noUse = true;
            object.notifyAll();
        }
    }

    private static void use() {
        synchronized (object) {
            while (!noUse) {
                try {
                    object.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            sleep(1);
            System.out.println("C: " + data);
            noUse = false;
            object.notifyAll();
        }
    }
}
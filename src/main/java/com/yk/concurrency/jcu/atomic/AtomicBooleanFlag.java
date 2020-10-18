package com.yk.concurrency.jcu.atomic;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-17 17:02
 **/
public class AtomicBooleanFlag {

    private final static AtomicBoolean flag = new AtomicBoolean(true);

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            while (flag.get()) {
                try {
                    Thread.sleep(1000);
                    System.out.println("I am working...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("I am finished...");
        }).start();

        Thread.sleep(5000);
        flag.set(false);
    }
}
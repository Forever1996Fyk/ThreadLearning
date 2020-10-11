package com.yk.concurrency.promote.chapter6;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-10 22:19
 **/
public class ReaderWorker extends Thread {
    private final SharedData data;

    public ReaderWorker(SharedData data) {
        this.data = data;
    }

    @Override
    public void run() {
        try {
            while (true) {
                char[] readBuff = data.read();
                System.out.println(Thread.currentThread().getName() + " reads " + String.valueOf(readBuff));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
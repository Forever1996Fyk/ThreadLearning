package com.yk.concurrency.base.chapter4;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-09-24 20:39
 **/
public class DaemonThread2 {

    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            // 在Thread里面再创建一个线程
            Thread innerThread = new Thread(() -> {
                try {
                    while (true) {
                        System.out.println("Do some thing for health check");
                        Thread.sleep(1_000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });


//            innerThread.setDaemon(true); // 如果Daemon设置为true, 就表示, 这个线程时守护线程, 而且必须要在start之前调用。
            innerThread.start();

            try {
                Thread.sleep(1_000);
                System.out.println("T Thread finish done.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
//        t.setDaemon(true);
        t.start();
    }
}
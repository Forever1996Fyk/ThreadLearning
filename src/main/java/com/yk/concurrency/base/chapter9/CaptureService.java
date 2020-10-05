package com.yk.concurrency.base.chapter9;

import java.util.*;

/**
 * @program: ThreadLearning
 * @description: 线程生产者消费者实战
 * @author: YuKai Fan
 * @create: 2020-10-05 20:28
 **/
public class CaptureService {

    private final static LinkedList<Control> CONTROLS = new LinkedList<>();

    private final static int MAX_WORKER = 5;

    public static void main(String[] args) {
        List<Thread> worker = new ArrayList<>();
        Arrays.asList("M1", "M2", "M3", "M4", "M5", "M6", "M7", "M8", "M9", "M10").stream()
                .map(CaptureService::createCaptureThread)
                .forEach(t -> {
                    t.start();
                    worker.add(t);
                });

        // 这里join操作必须要放在外面, 否则10个线程就会一个一个等待执行。放在外面是为了让main线程等待10线程结束再去执行
        worker.stream().forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Optional.of("All of capture work finished").ifPresent(System.out::println);
    }

    private static Thread createCaptureThread(String name) {
        return new Thread(() -> {
            Optional.of("The worker [" + Thread.currentThread().getName() + "] BEGIN capture data.").ifPresent(System.out::println);
            synchronized (CONTROLS) {
                // 这里控制 最大线程启动数
                while (CONTROLS.size() > MAX_WORKER) {
                    try {
                        CONTROLS.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                CONTROLS.addLast(new Control());
            }

            Optional.of("The worker [" + Thread.currentThread().getName() + "] is working...").ifPresent(System.out::println);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (CONTROLS) {
                Optional.of("The worker [" + Thread.currentThread().getName() + "] END capture data.").ifPresent(System.out::println);
                CONTROLS.removeFirst();
                // 这里使用notifyAll(), 因为后面的四个线程都已经处于等待状态, 所以需要唤醒所有的线程, 继续争夺锁资源
                CONTROLS.notifyAll();
            }

        }, name);
    }

    private static class  Control {

    }
}
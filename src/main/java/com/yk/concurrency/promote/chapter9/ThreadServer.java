package com.yk.concurrency.promote.chapter9;

import java.util.Random;

/**
 * @program: ThreadLearning
 * @description: 服务端, 不断的处理request, 每次处理都移除队列第一个request
 * @author: YuKai Fan
 * @create: 2020-10-11 11:52
 **/
public class ThreadServer extends Thread {

    private final RequestQueue queue;

    private final Random random;

    private volatile boolean flag = true;


    public ThreadServer(RequestQueue queue) {
        this.queue = queue;
        random = new Random(System.currentTimeMillis());
    }

    @Override
    public void run() {
        while (flag) {
            Request request = queue.getRequest();
            if (request == null) {
                System.out.println("Received th empty request.");
                continue;
            }
            System.out.println("Server -> " + request.getValue());
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public void close() {
        this.flag = false;
        this.interrupt();
    }
}
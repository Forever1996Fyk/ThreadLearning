package com.yk.concurrency.promote.chapter9;

import java.util.Random;

/**
 * @program: ThreadLearning
 * @description: 客户端, 不断地去发送request, 加入队列
 * @author: YuKai Fan
 * @create: 2020-10-11 11:50
 **/
public class ThreadClient extends Thread {

    private final RequestQueue queue;

    private final Random random;

    private final String value;

    public ThreadClient(RequestQueue queue, String value) {
        this.queue = queue;
        this.value = value;
        random = new Random(System.currentTimeMillis());
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("Client -> request " + value);
            queue.putRequest(new Request(value));
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
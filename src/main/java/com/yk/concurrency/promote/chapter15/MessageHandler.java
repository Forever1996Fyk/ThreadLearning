package com.yk.concurrency.promote.chapter15;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-11 16:58
 **/
public class MessageHandler {

    private final static Random random = new Random(System.currentTimeMillis());

    private final static Executor executor = Executors.newFixedThreadPool(5);

    public void request(Message message) {
        executor.execute(() -> {
            String value = message.getValue();
            try {
                Thread.sleep(random.nextInt(1000));
                System.out.println("The message will be handle by " + Thread.currentThread().getName() + " " + value);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public void shutdown() {
        ((ExecutorService)executor).shutdown();
    }
}
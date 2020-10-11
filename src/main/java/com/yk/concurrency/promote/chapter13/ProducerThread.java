package com.yk.concurrency.promote.chapter13;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-11 16:12
 **/
public class ProducerThread extends Thread {

    private final MessageQueue messageQueue;

    private final static Random random = new Random(System.currentTimeMillis());

    private final static AtomicInteger counter = new AtomicInteger(0);

    public ProducerThread(MessageQueue messageQueue, int seq) {
        super("producer" + seq);
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message message = new Message("Message-" + counter.getAndIncrement());
                messageQueue.put(message);
                System.out.println(Thread.currentThread().getName() + " put message " + message.getData());
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
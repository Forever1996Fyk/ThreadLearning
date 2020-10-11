package com.yk.concurrency.promote.chapter13;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-11 16:18
 **/
public class ProducerAndConsumerClient {

    public static void main(String[] args) {
        final MessageQueue messageQueue = new MessageQueue();

        new ProducerThread(messageQueue, 1).start();
        new ProducerThread(messageQueue, 2).start();
        new ProducerThread(messageQueue, 3).start();
        new ConsumerThread(messageQueue, 1).start();
        new ConsumerThread(messageQueue, 2).start();
    }
}
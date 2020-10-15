package com.yk.concurrency.promote.chapter16;

import java.util.LinkedList;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-12 21:17
 **/
public class ActivationQueue {

    private final static int MAX_METHOD_REQUEST_QUEUE_SIZE = 100;

    private final LinkedList<MethodRequest> methodQueue;

    public ActivationQueue() {
        this.methodQueue = new LinkedList<>();
    }

    public synchronized void put(MethodRequest request) {
        while (methodQueue.size() >= MAX_METHOD_REQUEST_QUEUE_SIZE) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.methodQueue.addLast(request);
        this.notifyAll();
    }

    public synchronized MethodRequest take() {
        while (methodQueue.isEmpty()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        MethodRequest request = methodQueue.removeFirst();
        return request;
    }
}
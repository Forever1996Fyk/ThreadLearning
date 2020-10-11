package com.yk.concurrency.promote.chapter9;

import java.util.LinkedList;

/**
 * @program: ThreadLearning
 * @description: 这是一种比较简单的设计模式, 当一个线程在处理一个不可放弃的任务时, 此时又来一个新的任务, 就让新的任务放入队列, 当处理完当前任务时, 再去处理
 * @author: YuKai Fan
 * @create: 2020-10-11 11:44
 **/
public class RequestQueue {
    private final LinkedList<Request> queue = new LinkedList<>();

    public Request getRequest() {
        synchronized (queue) {
            while (queue.size() <= 0) {
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    return null;
                }
            }
            return queue.removeFirst();
        }
    }

    public void putRequest(Request request) {
        synchronized (queue) {
            queue.addLast(request);
            queue.notifyAll();
        }
    }
}
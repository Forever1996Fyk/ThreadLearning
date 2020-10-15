package com.yk.concurrency.promote.chapter16;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-12 21:21
 **/
public class SchedulerThread extends Thread {

    private final ActivationQueue activationQueue;

    public SchedulerThread(ActivationQueue activationQueue) {
        this.activationQueue = activationQueue;
    }

    public void invoke(MethodRequest request) {
        this.activationQueue.put(request);
    }

    @Override
    public void run() {
        while (true) {
            activationQueue.take().execute();
        }
    }
}
package com.yk.concurrency.promote.chapter14;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-11 16:49
 **/
public class CountDown {

    private final int total;

    private int counter = 0;

    public CountDown(int total) {
        this.total = total;
    }

    public void down() {
        synchronized (this) {
            this.counter++;
            this.notifyAll();
        }
    }

    public void await() throws InterruptedException {
        synchronized (this) {
            while (counter != total) {
                this.wait();
            }
        }
    }
}
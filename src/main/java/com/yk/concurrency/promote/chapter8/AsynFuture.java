package com.yk.concurrency.promote.chapter8;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-11 10:54
 **/
public class AsynFuture<T> implements  Future<T> {

    private volatile boolean done = false;

    private T result;

    public void done(T result) {
        synchronized (this) {
            this.result = result;
            this.done = true;
            this.notifyAll();
        }
    }

    @Override
    public T get() throws InterruptedException {
        synchronized (this) {
            while (!done) {
                this.wait();
            }
        }
        return result;
    }
}
package com.yk.concurrency.promote.chapter16;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-12 20:59
 **/
public class FutureResult implements Result {

    private Result result;

    private boolean ready = false;

    public synchronized void setResult(Result result) {
        this.result = result;
        this.ready = true;
        this.notifyAll();
    }

    @Override
    public Object getResultValue() {
        synchronized (this) {
            while (!ready) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return this.result.getResultValue();
        }
    }
}
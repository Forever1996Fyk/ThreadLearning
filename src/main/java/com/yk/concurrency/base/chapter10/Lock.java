package com.yk.concurrency.base.chapter10;

import java.util.Collection;
import java.util.concurrent.TimeoutException;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-05 21:08
 **/
public interface Lock {
    class TimeOutException extends Exception {
        public TimeOutException(String message) {
            super(message);
        }
    }

    void lock() throws InterruptedException;

    void lock(long mills) throws InterruptedException, TimeoutException;

    void unlock();

    Collection<Thread> getBlockedThread();

    int getBlockedSize();
}
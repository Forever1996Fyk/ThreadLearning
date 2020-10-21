package com.yk.concurrency.jcu.utils.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-21 23:35
 **/
public class ReentrantLockExample {

    private final Lock lock = new ReentrantLock();

    public static void main(String[] args) {

    }

    public void needLock() {

    }
}
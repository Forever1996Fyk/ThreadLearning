package com.yk.concurrency.base.chapter8;

/**
 * @program: ThreadLearning
 * @description: 死锁
 * @author: YuKai Fan
 * @create: 2020-09-28 21:27
 **/
public class DeadLock {

    private OtherService otherService;

    public DeadLock(OtherService otherService) {
        this.otherService = otherService;
    }

    private final Object lock = new Object();

    public void m1() {
        synchronized (lock) {
            System.out.println("m1");
            otherService.s1();
        }
    }

    public void m2() {
        synchronized (lock) {
            System.out.println("m2");
        }
    }
}
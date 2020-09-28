package com.yk.concurrency.base.chapter8;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-09-28 21:32
 **/
public class DeadLockTest {

    /**
     * 死锁的产生:
     *
     * 当m1 持有 obj1的锁, 执行代码时需要去获取 obj2的锁, 而m2持有obj2的锁, 但是执行代码时需要去获取obj1的锁,
     *
     * 这就导致了, m1持有obj1的锁, 但是需要获取obj2的锁, m2持有obj2的锁, 但是需要获取obj1的锁, 这就产生了一个死循环。从而产生死锁.
     *
     * 所以如果不想产生死锁, 在代码中最好不要使用synchronized的嵌套。
     * @param args
     */
    public static void main(String[] args) {
        OtherService otherService = new OtherService();
        DeadLock deadLock = new DeadLock(otherService);
        otherService.setDeadLock(deadLock);

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    deadLock.m1();
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    otherService.s2();
                }
            }
        }.start();
    }
}
package com.yk.concurrency.base.chapter3;

import java.util.Arrays;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-09-23 21:25
 **/
public class CreateThread {

    public static void main(String[] args) {

        /**
         * 如果构造线程对象时, 未传入ThreadGroup, Thread会默认获取父线程的ThreadGroup作为该线程的ThreadGroup,
         * 此时子线程和父线程将会在同一个ThreadGroup中, 可以通过ThreadGroup 获取有多少个线程在运行
         */
        Thread t1 = new Thread();
        t1.start();
//        t1.getThreadGroup();
//        System.out.println(t1.getThreadGroup());
//        System.out.println(Thread.currentThread().getName());
//        System.out.println(Thread.currentThread().getThreadGroup().getName());

        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();

        /**
         * 此时会有三个线程打印, 因为还有一个监控线程 Thread[Monitor Ctrl-Break,5,main]
         */
        System.out.println(threadGroup.activeCount());

        Thread[] threads = new Thread[threadGroup.activeCount()];
        threadGroup.enumerate(threads);

        Arrays.asList(threads).forEach(System.out::println);
    }
}
package com.yk.concurrency.jcu.atomic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-12 22:09
 **/
public class AtomicIntegerTest {

    /**
     * 1. 保证可见性
     * 2. 有序性, 加内存屏障
     * 3. 不能保证原子性
     */
//    private static volatile int value = 0;

    private static Set<Integer> set = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) throws InterruptedException {
        /*Thread t1 = new Thread() {
            @Override
            public void run() {
                int x = 0;
                while (x < 500) {
                    set.add(value);
                    int temp = value;
                    System.out.println(Thread.currentThread().getName() + ":" + temp);
                    value += 1;
                    x++;
                }
            }
        };

        Thread t2 = new Thread() {
            @Override
            public void run() {
                int x = 0;
                while (x < 500) {
                    set.add(value);
                    int temp = value;
                    System.out.println(Thread.currentThread().getName() + ":" + temp);
                    value += 1;
                    x++;
                }
            }
        };

        Thread t3 = new Thread() {
            @Override
            public void run() {
                int x = 0;
                while (x < 500) {
                    set.add(value);
                    int temp = value;
                    System.out.println(Thread.currentThread().getName() + ":" + temp);
                    value += 1;
                    x++;
                }
            }
        };

        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();

        System.out.println("The Set Size:" + set.size());*/

        final AtomicInteger value = new AtomicInteger();
        Thread t1 = new Thread() {
            @Override
            public void run() {
                int x = 0;
                while (x < 500) {
                    int v = value.getAndIncrement();
                    set.add(v);
                    System.out.println(Thread.currentThread().getName() + ":" + v);
                    x++;
                }
            }
        };

        Thread t2 = new Thread() {
            @Override
            public void run() {
                int x = 0;
                while (x < 500) {
                    int v = value.getAndIncrement();
                    set.add(v);
                    System.out.println(Thread.currentThread().getName() + ":" + v);
                    x++;
                }
            }
        };

        Thread t3 = new Thread() {
            @Override
            public void run() {
                int x = 0;
                while (x < 500) {
                    int v = value.getAndIncrement();
                    set.add(v);
                    System.out.println(Thread.currentThread().getName() + ":" + v);
                    x++;
                }
            }

        };

        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();

        System.out.println("The Set Size:" + set.size());

    }
}
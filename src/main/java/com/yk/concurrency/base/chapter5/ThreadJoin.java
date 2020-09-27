package com.yk.concurrency.base.chapter5;

import java.util.Optional;
import java.util.stream.IntStream;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-09-27 20:24
 **/
public class ThreadJoin {

    public static void main(String[] args) throws InterruptedException {
        /**
         * 先执行t1完之后, 在执行main线程
         */
        Thread t1 = new Thread(() -> {
            IntStream.range(1, 1000)
                    .forEach(i -> System.out.println(Thread.currentThread().getName() + "->" + i));
        });

        Thread t2 = new Thread(() -> {
            IntStream.range(1, 1000)
                    .forEach(i -> System.out.println(Thread.currentThread().getName() + "->" + i));
        });

        t1.start();

        t2.start();

        /**
         * 当前线程执行完, 线程死了之后 才会执行后面的线程, 必须要在start()后调用
         *
         * 注意: 这里当前线程指的是main线程, 也就是说上面的两个线程依然会交叉输入, t1, t2都执行结束之后, 在执行main线程的代码.
         *
         * 所以join可以理解为, 在所有子线程都执行完的情况下, 在执行main线程
         */
        t1.join();
        t2.join();

        Optional.of("All of tasks finish done.").ifPresent(System.out::println);
        IntStream.range(1, 1000)
                .forEach(i -> System.out.println(Thread.currentThread().getName() + "->" + i));
    }
}
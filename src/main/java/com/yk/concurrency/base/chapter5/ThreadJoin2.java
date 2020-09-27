package com.yk.concurrency.base.chapter5;

import java.util.Optional;
import java.util.stream.IntStream;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-09-27 20:35
 **/
public class ThreadJoin2 {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            System.out.println("t1 is running");
            try {
                Thread.sleep(10_000);
                System.out.println("t1 is done");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();

        /**
         * 参数分别是100毫秒, 10纳秒。意思就是t1线程执行100毫秒, 10纳秒后, 还没执行完, 就直接执行main线程
         */
        t1.join(100, 10);

        Optional.of("All of tasks finish done.").ifPresent(System.out::println);
        IntStream.range(1, 1000)
                .forEach(i -> System.out.println(Thread.currentThread().getName() + "->" + i));
    }
}
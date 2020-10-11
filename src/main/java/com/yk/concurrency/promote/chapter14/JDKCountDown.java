package com.yk.concurrency.promote.chapter14;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

/**
 * @program: ThreadLearning
 * @description: 如果程序是分阶段性去处理任务, 例如: 多个线程处理阶段1完成后, 再去处理阶段2,...
 *               之前我们使用的是join方式。
 *
 * @author: YuKai Fan
 * @create: 2020-10-11 16:30
 **/
public class JDKCountDown {

    private static final Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(5);
        System.out.println("准备多线程处理任务.");

        IntStream.rangeClosed(1, 5).forEach(i -> new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " is working.");
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
        }, String.valueOf(i)).start());

        latch.await();
        System.out.println("多线程任务全部结束, 准备第二阶段任务");
        System.out.println(".............");
        System.out.println("finish");
    }
}
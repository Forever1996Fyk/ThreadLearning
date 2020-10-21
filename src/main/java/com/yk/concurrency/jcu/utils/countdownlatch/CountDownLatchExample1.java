package com.yk.concurrency.jcu.utils.countdownlatch;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: ThreadLearning
 * @description: CountDownLatch的用途很大, 在业务场景中, 有时我们需要先执行方法1, 在执行方法2, 最后执行方法3。
 *               这三个方法执行时串行化的，如果方法1暂用时间太长, 我们可以使用线程池, 用多个线程去处理方法1, 处理完结果之后再去执行方法2, 方法3。也就是异步执行
 *               如果方法3需要在方法1执行完之后才能执行, 那么CountDownLatch就能派上用场。
 *
 *               如果在某个一系列串行化的过程中, 发现中间某个部分是可并行化的, 那么我们就可以用并行化的方式去做提高处理速度, 最终在串行化。那么CountDownLatch就可以使用了
 * @author: YuKai Fan
 * @create: 2020-10-19 22:00
 **/
public class CountDownLatchExample1 {
    private static Random random = new Random(System.currentTimeMillis());

    private static ExecutorService executor = Executors.newFixedThreadPool(2);

    private static final CountDownLatch latch = new CountDownLatch(10);

    public static void main(String[] args) throws InterruptedException {
        //1. 获取数据
        int[] data = query();
        //2. 利用数据进行处理
        for (int i = 0; i < data.length; i++) {
            executor.execute(new SimpleRunnable(data, i));
        }
        //3.

        // executor的shutdown()方法只会在线程池空闲的时候才会中断, 也就是说shutdown()其实也是异步方法。不能用这种方式来控制线程执行的顺序
//        executor.shutdown();

        latch.await();
        System.out.println("all of work finish done.");

    }

    static class SimpleRunnable implements Runnable {

        private final int[] data;

        private final int index;

        SimpleRunnable(int[] data, int index) {
            this.data = data;
            this.index = index;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(random.nextInt(2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int value = data[index];
            if (value % 2 == 0) {
                data[index] = value * 2;
            } else {
                data[index] = value * 10;
            }

            System.out.println(Thread.currentThread().getName() + " finished.");
            latch.countDown();
        }
    }

    private static int[] query() {
        return new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    }
}
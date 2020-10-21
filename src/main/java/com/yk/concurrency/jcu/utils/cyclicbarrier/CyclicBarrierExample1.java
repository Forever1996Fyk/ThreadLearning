package com.yk.concurrency.jcu.utils.cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-20 21:22
 **/
public class CyclicBarrierExample1 {

    public static void main(String[] args) throws InterruptedException {
//        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

        // 这个runnable接口时当所有线程执行完成后, 进行回调
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2, new Runnable() {
            @Override
            public void run() {
                System.out.println("all of finished");
            }
        });

        new Thread() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(15);
                    System.out.println("T1 finished.");
                    cyclicBarrier.await();
                    System.out.println("T1 the other thread finished to.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(10);
                    System.out.println("T2 finished.");
                    cyclicBarrier.await();
                    System.out.println("T2 the other thread finished to.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        while (true) {
            System.out.println(cyclicBarrier.getNumberWaiting());
            System.out.println(cyclicBarrier.getParties());
            System.out.println(cyclicBarrier.isBroken());
            TimeUnit.SECONDS.sleep(2);
        }
    }
}
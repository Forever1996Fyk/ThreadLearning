package com.yk.concurrency.jcu.utils.cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-20 21:56
 **/
public class CyclicBarrierExample2 {

    /**
     * 这段程序中有三个线程, 当前两个线程启动时, t2线程直接进行await(), 于是cyclicBarrier的parties数减一
     *
     * 而t1线程还在处理任务, main线程在等待100ms之后, 调用了cyclicBarrier.reset(); 此时线程t1还未调用await()方法, 也就是未处理完
     * 导致 t1 抛出异常 BrokenBarrierException,
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

        Thread t1 = new Thread() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(5);
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread t2 = new Thread() {
            @Override
            public void run() {
                try {
//                    TimeUnit.SECONDS.sleep(5);
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        };

        t1.start();
        t2.start();

        TimeUnit.MICROSECONDS.sleep(100);

        // reset()方法会让cyclicBarrier的parties值恢复初始值
        // reset==>initial==>finished
        cyclicBarrier.reset();
    }
}
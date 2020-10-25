package com.yk.concurrency.jcu.utils.phaser;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-24 21:49
 **/
public class PhaserExample5 {

    private static Random random = new Random(System.currentTimeMillis());

    /**
     * arrive()方法, 是当线程执行到这个方法时, 即使线程数量没有达到parties的数量, 但是依然可以去执行之后的任务, 并不会处于等待状态。
     * 但是在主线程中有arriveAndAwaitAdvance()方法, 在主线程中, 只有当5个线程都达到时, main线程才会执行
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        final Phaser phaser = new Phaser(5);

        for (int i = 0; i < 4; i++) {
            new ArriveTask(phaser, i).start();
        }

        phaser.arriveAndAwaitAdvance();

        System.out.println("The phase 1 work finished done.");

    }

    private static class ArriveTask extends  Thread {
        private final Phaser phaser;

        private ArriveTask(Phaser phaser, int no) {
            super(String.valueOf(no));
            this.phaser = phaser;
        }

        @Override
        public void run() {
            System.out.println(getName() + " start working.");
            PhaserExample5.sleep();
            System.out.println(getName() + " The phase one is running");
            phaser.arrive();

            PhaserExample5.sleep();

            System.out.println(getName() + " keep to do other thing.");
        }
    }

    private static void sleep() {
        try {
            TimeUnit.SECONDS.sleep(random.nextInt(5));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
package com.yk.concurrency.jcu.utils.phaser;

import java.sql.Time;
import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-24 16:42
 **/
public class PhaserExample2 {

    private final static Random random = new Random(System.currentTimeMillis());

    /**
     * 需求: 多个运动员需要完成running, bicycle, long jump等运动。 但是每个运动员都需要同时完成同一种运动之后，才能去进行下一类型的运动。
     *
     * 例如: 有三个运动员， 这三个运动员必须都完成running之后, 才能开始bicycle
     *
     * 通过这个例子, 可以发现Phaser基本上可以实现CountDownLatch, CyclicBarrier的功能了。
     *
     *
     * Phaser构造函数中的参数parties是参与者线程的数量。
     * 也就是说, parties=5时, 表示当有5个线程都执行到phaser.arriveAndAwaitAdvance();方法时,才会开始执行下面的任务。而如果没有5个线程, 那么就会一直等待
     * @param args
     */
    public static void main(String[] args) {
        final Phaser phaser = new Phaser(5);

        for (int i = 1; i < 6; i++) {
            new Athletes(phaser, i).start();
        }
    }

    static class Athletes extends Thread {
        private final Phaser phaser;

        private final int no;

        Athletes(Phaser phaser, int no) {
            this.phaser = phaser;
            this.no = no;
        }

        @Override
        public void run() {
            try {
                System.out.println(no + ": start running.");
                TimeUnit.SECONDS.sleep(random.nextInt(5));
                System.out.println(no + ": end running.");

                System.out.println("getPhase ==>" + phaser.getPhase());

                phaser.arriveAndAwaitAdvance();

                System.out.println(no + ": start bicycle.");
                TimeUnit.SECONDS.sleep(random.nextInt(5));
                System.out.println(no + ": end bicycle.");

                System.out.println("getPhase ==>" + phaser.getPhase());


                phaser.arriveAndAwaitAdvance();

                System.out.println(no + ": start long jump.");
                TimeUnit.SECONDS.sleep(random.nextInt(5));
                System.out.println(no + ": end long jump.");

                System.out.println("getPhase ==>" + phaser.getPhase());


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
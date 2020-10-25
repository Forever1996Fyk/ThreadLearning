package com.yk.concurrency.jcu.utils.phaser;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-24 20:00
 **/
public class PhaserExample3 {

    private final static Random random = new Random(System.currentTimeMillis());

    /**
     * 需求: 多个运动员需要完成running, bicycle, long jump等运动。 但是每个运动员都需要同时完成同一种运动之后，才能去进行下一类型的运动。
     * <p>
     * 例如: 有三个运动员， 这三个运动员必须都完成running之后, 才能开始bicycle
     * <p>
     * 通过这个例子, 可以发现Phaser基本上可以实现CountDownLatch, CyclicBarrier的功能了。
     * <p>
     * 但是问题来了, 如果其中一个运动员, running阶段没有完成, 也就是抛出了异常, 会出现问题。后续的所有操作都会在出现异常的那部分任务等待, 无法退出。
     * 所以需要在出现异常的地方调用phaser.arriveAndDeregister()方法取消线程对异常任务的执行
     *
     * @param args
     */
    public static void main(String[] args) {
        final Phaser phaser = new Phaser(5);

        for (int i = 1; i < 5; i++) {
            new Athletes(phaser, i).start();
        }

        new InjuredAthletes(phaser, 5).start();
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
                sport(phaser,no + ": start running.", no + ": end running.");
                sport(phaser,no + ": start bicycle.", no + ": end bicycle.");
                sport(phaser,no + ": start long jump.", no + ": end long jump.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 受伤运动员
     */
    static class InjuredAthletes extends Thread {
        private final Phaser phaser;

        private final int no;

        InjuredAthletes(Phaser phaser, int no) {
            this.phaser = phaser;
            this.no = no;
        }

        @Override
        public void run() {
            try {
                sport(phaser,no + ": start running.", no +": end running.");
                sport(phaser,no + ": start bicycle.", no + ": end bicycle.");

//                System.out.println("Oh shit, I am injured.");

                // 在线程处理任务出现异常时, phaser调用arriveAndDeregister方法取消后面的执行, 否则程序就会一直处于等待状态
                System.out.println("Oh shit, I am injured, I will be exited");
                phaser.arriveAndDeregister();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private static void sport(Phaser phaser, String s1, String s2) throws InterruptedException {
        System.out.println(s1);
        TimeUnit.SECONDS.sleep(random.nextInt(5));
        System.out.println(s2);

        phaser.arriveAndAwaitAdvance();
    }
}
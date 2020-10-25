package com.yk.concurrency.jcu.utils.phaser;

import com.google.common.primitives.Ints;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-24 22:13
 **/
public class PhaserExample6 {

    /**
     * awaitAdvance(int parties), 如果这个awaitAdvance方法的parties, 与定义Phaser的parties相同的话, 那么当所有线程都达到这个awaitAdvance()方法就是处于等待状态
     * 但是如果不相同, 那么就会立即返回。
     *
     * 如果我想开启6个phaser, 用来监控Task
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        final Phaser phaser = new Phaser(7);

        IntStream.rangeClosed(0, 5).boxed().map(i -> phaser).forEach(AwaitAdvanceTask::new);

        phaser.awaitAdvance(phaser.getPhase());
        System.out.println("==============");
    }

    private static class AwaitAdvanceTask extends Thread {
        private final Phaser phaser;

        private AwaitAdvanceTask(Phaser phaser) {
            this.phaser = phaser;
        }

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            phaser.arrive();
            System.out.println(getName() + " finished work.");
        }
    }
}
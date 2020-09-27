package com.yk.concurrency.base.chapter6;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-09-27 21:45
 **/
public class ThreadCloseGraceful2 {

    private static class Worker extends Thread {
        @Override
        public void run() {
            while (true) {
//                try {
//                    Thread.sleep(1);
//                } catch (InterruptedException e) {
//                    break; //return
//                }

                if (Thread.interrupted()) {
                    break;
                }

                // to do something
            }
        }
    }

    public static void main(String[] args) {
        Worker worker = new Worker();
        worker.start();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        worker.interrupt();
    }
}
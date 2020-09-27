package com.yk.concurrency.base.chapter6;

/**
 * @program: ThreadLearning
 * @description: 强制关闭线程
 * @author: YuKai Fan
 * @create: 2020-09-27 21:49
 **/
public class ThreadCloseForce {
    private static class Worker extends Thread {
        private boolean flag = true;
        @Override
        public void run() {
            while (flag) {
                /**
                 * 例如进行网络连接的操作, 你可能期望是3s之内连接成功, 但是可能经过了30s也没成功, 导致这个线程阻塞了。
                 *
                 * 如果在操作中block了, 这样就无法用interrupt中断, 也没有机会去改变flag。 这就需要强制结束线程。
                 *
                 * 但是在jdk中只有stop可以强制结束线程, 但是stop()方法已经废弃了。所以需要自己实现方法。详见ThreadService
                 */
            }

            // to do something
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
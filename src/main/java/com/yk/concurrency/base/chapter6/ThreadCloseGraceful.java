package com.yk.concurrency.base.chapter6;

/**
 * @program: ThreadLearning
 * @description: 优雅的关闭线程
 * @author: YuKai Fan
 * @create: 2020-09-27 21:39
 **/
public class ThreadCloseGraceful {

    /**
     * 使用场景: 例如程序中你需要批量导入数据, 但是导入的时间比较长, 所以使用多线程去处理。
     *
     * 创建多个线程分别去获取数据, 但是如果在线程执行过程中出现问题, 可能线程并没有停止, 而是阻塞。那这样就会造成资源的浪费。
     *
     * 通过这种方式去关闭线程。也就是判断true, false。是否执行代码
     */
    private static class Worker extends Thread {
        private volatile boolean start = true;
        @Override
        public void run() {
            while (start) {
                //
            }
        }

        public void shutdown() {
            this.start = false;
        }
    }

    public static void main(String[] args) {
        Worker worker = new Worker();
        worker.start();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        worker.shutdown();
    }
}
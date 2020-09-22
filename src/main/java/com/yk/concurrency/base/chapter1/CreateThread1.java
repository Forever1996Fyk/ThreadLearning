package com.yk.concurrency.base.chapter1;

/**
 * @program: ThreadLearning
 * @description: 创建线程方式1: 创建Thread类, 并重写run方法
 * @author: YuKai Fan
 * @create: 2020-09-22 22:13
 **/
public class CreateThread1 {
    public static void main(String[] args) {

        // t1线程执行
        Thread t1 = new Thread("custom-thread") {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    System.out.println("Task 1=>" + i);
                    try {
                        Thread.sleep(1000 * 30L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        /**
         * 注意, new Thread()只是创建一个java的实例而已, 并不代表是一个线程, 必须要调用start方法才会立即启动线程
         *
         * 而且 这个start方法是不能执行两次。
         *
         * 通过分析Thread的start()源码:
         *
         * start()会调用一个 native方法start0() 这是一个c++的方法, 在这个start0()方法中会调用 run()方法。
         *
         * 这个run()方法其实是用了模板方法的方式, 可以重写run()方法。让这个start0()方法去执行被覆盖的run()方法
         *
         */
        t1.start();

        // main线程执行
        for (int j = 0; j < 100; j++) {
            System.out.println("Task 2=>" + j);
        }

        /**
         * 线程的生命周期: new, runnable, running, block, terminate
         *
         * 1. new Thread()创建线程。(不管是后面的线程池还是join 这是最基本的创建线程的第一步)
         *
         * 2. 调用 start() 方法。 当前线程启动了, 处于runnable状态, 即可执行状态, 具备执行的能力, 但并不意味立即开始执行
         *
         * 3. 当线程被cpu调度dispatch, 此时是running状态, 即执行状态。(在running状态中，有可能会变成runnable状态。因为cpu调度到其他线程, 此时状态变成runnable)
         *
         * 4. 如果在running过程中, 该线程被blocked, 例如: sleep, 此时线程处于阻塞状态。(注意: 如果被blocked后, 无法立即回到running状态, 必须要先到runnable状态)
         *
         * 5. 在线程在running状态正常结束, 或者在blocked状态中被打断, 在runnable状态中由于某些原因导致线程死亡, 就会进入终结terminated状态
         */

        // execute();

    }

    private static void execute() {
        /**
         * 通过这个创建线程的方式执行代码
         */
        new Thread("read-data-thread"){
            @Override
            public void run() {
                Common.readFromDataBase();
            }
        }.start();

        new Thread("write-file-thread"){
            @Override
            public void run() {
                Common.writeDataToFile();
            }
        }.start();
    }
}
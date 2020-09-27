package com.yk.concurrency.base.chapter6;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-09-27 20:59
 **/
public class ThreadInterrupt {
    private static final Object object = new Object();
    public static void main(String[] args) {
        /*Thread t = new Thread() {
            @Override
            public void run() {
                while (true) {
                    synchronized (object) {
                        try {
                            object.wait(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            System.out.println(this.isInterrupted());
                        }
                    }
                }
            }
        };

        t.start();
        Thread.sleep(100);
        System.out.println(t.isInterrupted());
        t.interrupt();
        System.out.println(t.isInterrupted());*/

        /**
         * interrupted() 方法与 isInterrupted() 方法的用途是一样的。只不过一个是当前线程静态方法, 一个是普通方法
         */
        /*Thread t = new Thread(() -> {
            synchronized (object) {
                try {
                    object.wait(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println(Thread.interrupted());
                }
            }
        });*/

        /**
         * 下面的代码中, 输出 interrupt 之后, 虽然执行了t.interrupt() 但是程序并不会结束。
         *
         * 即使执行t.interrupt()方法, t中断, 但是执行t.join()并不会抛出异常。
         *
         * 因为join是针对的main线程, 所以t.join(), 实际上join的是main线程。也就是说join的线程指的是main线程。而这个过程main线程并没有收到影响所以不会打断程序, 也不会抛出异常。
         *
         * 如果想要打断, 就必须执行 main.interrupt()。这样才会抛出异常
         */
        Thread t = new Thread() {
            @Override
            public void run() {
                while (true) {

                }
            }
        };

        t.start();
        Thread t2 = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                t.interrupt();
                System.out.println("interrupt");
            }
        };
        t2.start();

        try {
            t.join(); // 注意: join内部其实是main线程在执行, 不是t线程
        } catch (InterruptedException e) {

            // 上面的执行过程, 并不会执行到这里
            e.printStackTrace();
        }
    }
}
package com.yk.concurrency.base.chapter11;

/**
 * @program: ThreadLearning
 * @description: 在线程运行期间捕获异常
 * @author: YuKai Fan
 * @create: 2020-10-05 22:28
 **/
public class ThreadException {
    private final static int A = 10;
    private final static int B = 0;

    public static void main(String[] args) {
        /*Thread t = new Thread(() -> {
            try {
                Thread.sleep(5_000L);
                int result = A / B; // 这个异常是无法抛出的, 只能在控制台打印, 或者在日志中记录
                System.out.println(result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        //这个方法可以捕获线程中的异常
        t.setUncaughtExceptionHandler((thread, e) -> {
            System.out.println(e);
            System.out.println(thread);
        });
        t.start();*/

        new Test1().test();




    }
}
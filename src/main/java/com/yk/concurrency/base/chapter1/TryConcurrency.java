package com.yk.concurrency.base.chapter1;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-09-22 21:54
 **/
public class TryConcurrency {

    /**
     * 在调用main函数时, 其实系统已经启动了一个主线程, 用来执行对应的程序
     *
     * JVM 在调用main函数时, 会创建一个main线程, 被JVM调用。 这是一个非守护线程(守护线程会在后面解释)
     *
     * 但是JVM不仅仅只有main线程, 还是很多守护线程, 例如：Finalizer垃圾回收线程, RMI线程等
     * @param args
     */
    public static void main(String[] args) {

        // 在只执行下面两个方法的情况下, 必须要先执行第一个方法, 在执行第二个方法, 效率很低
        /*
        Common.readFromDataBase();
        Common.writeDataToFile();
        */

        try {
            Thread.sleep(1000 * 30L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
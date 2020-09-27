package com.yk.concurrency.base.chapter3;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-09-23 22:05
 **/
public class CreateThread2 {

    /**
     * int i = 0; 是在 栈内存--方法区
     */
    private int i = 0;

    /**
     * byte 也是 栈内存——方法区 , 但是具体的数据是放在堆内存
     */
    private byte[] bytes = new byte[1024];

    private static int counter = 0;

    /**
     * 执行main函数也会开启一个线程: JVM will create a thread named "main": 这个main线程会在虚拟机栈中存在。
     * @param args
     */
    public static void main(String[] args) {
        /*

        // 创建 JVM stack 虚拟机栈

        // j 就存放在 虚拟机栈——局部变量表
        int j = 0;

        // arr 也会存放在局部变量表(存放地址), 但是数据还是存放在堆内存
        int[] arr = new int[1024];

        */


        // create JVM stack
        try {

            /**
             *  每一次执行add()操作, 都是在虚拟机栈中执行栈操作, 压栈。在无限制的进行压栈操作, 会导致StackOverFlowError栈溢出错误。
             *
             *  这里跟Thread的构造函数: Thread(ThreadGroup threadGroup, Runnable target, String name, long stackSize) 有关系
             *
             *  这里的 stackSize的值, 可以在自己定义的线程中指定该线程在虚拟机栈中所占的大小。
             *
             */
            add(0);
        } catch (Error e) {
            e.printStackTrace();
            System.out.println(counter);
        }
    }

    private static void add(int i) {
        ++counter;
        add(i + 1);
    }

}
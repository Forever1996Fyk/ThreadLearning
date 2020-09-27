package com.yk.concurrency.base.chapter3;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-09-23 22:40
 **/
public class CreateThread4 {
    private static int counter = 1;

    /**
     * stackSize 越小的情况下, 线程可创建的数量越大。反之也是则线程可创建的数量越小。
     *
     * 如果 stackSize为0, 则表示这个参数就会被忽略, 该参数会被JNI函数去使用
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                counter++;
                new Thread(() -> {
                    while (true) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        } catch (Error e) {

        }

        System.out.println("Total Created Thread Nums => " + counter);
    }
}
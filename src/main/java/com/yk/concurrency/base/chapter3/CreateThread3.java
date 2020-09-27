package com.yk.concurrency.base.chapter3;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-09-23 22:21
 **/
public class CreateThread3 {

    private static int counter = 1;

    /**
     * 注意: stackSize 参数在一些平台有效, 在另一些平台无效
     * @param args
     */
    public static void main(String[] args) {
        Thread thread = new Thread(null, new Runnable() {
            @Override
            public void run() {
                try {
                    add(1);
                } catch (Error e) {
                    System.out.println(counter);
                }
            }

            private void add(int i) {
                counter++;
                add(i + 1);
            }
        }, "Test", 1 << 24);

        thread.start();
    }
}
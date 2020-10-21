package com.yk.concurrency.jcu.utils.exchanger;

import java.util.concurrent.Exchanger;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-21 22:13
 **/
public class ExchangeExample2 {

    /**
     * 通过这个例子可以发现, Exchanger交换数据时, 并不是数据的拷贝, 而是同一个数据。这样两个线程同时操作一个数据就会出现线程安全问题。
     * @param args
     */
    public static void main(String[] args) {
        final Exchanger<Object> exchanger = new Exchanger<>();

        new Thread() {
            @Override
            public void run() {
                Object aObj  = new Object();
                System.out.println("A will send the object " + aObj);

                try {
                    Object rObj = exchanger.exchange(aObj);
                    System.out.println("A recievd the object " + rObj);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                Object bObj  = new Object();
                System.out.println("B will send the object " + bObj);

                try {
                    Object rObj = exchanger.exchange(bObj);
                    System.out.println("B recievd the object " + rObj);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
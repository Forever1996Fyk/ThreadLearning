package com.yk.concurrency.jcu.utils.exchanger;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @program: ThreadLearning
 * @description: Exchange 交换线程间的数据
 * @author: YuKai Fan
 * @create: 2020-10-21 21:49
 **/
public class ExchangeExample1 {

    /**
     * V r = exchange(V v)
     * 参数v表示当前线程传入的值
     * 返回值r表示对方其他线程返回的值
     *
     * 要注意的是参数v是发给其他线程的数据, 返回值r是其他线程发送的数据
     *
     * 注意:
     * 1. 其中一个线程在等待对方线程数据时Time out, 那么另一个线程可能就会一直处于等待状态
     *
     * 2. exchange一定是成对出现的, 也就是说只能有两个线程进行数据交换
     *
     * @param args
     */
    public static void main(String[] args) {
        final Exchanger<String> exchanger = new Exchanger<>();

        new  Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " start.");
                try {
                    String result = exchanger.exchange("I am come from T-A", 10,  TimeUnit.SECONDS);
                    System.out.println(Thread.currentThread().getName() + " Get value [" + result + "]");
                } catch (InterruptedException | TimeoutException e) {
                    e.printStackTrace();
                    System.out.println("Time out");
                }

                System.out.println(Thread.currentThread().getName() + " end.");
            }
        }, "==A==").start();

        new  Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " start.");
                try {
                    TimeUnit.SECONDS.sleep(20);
                    String result = exchanger.exchange("I am come from T-B");
                    System.out.println(Thread.currentThread().getName() + " Get value [" + result + "]");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().getName() + " end.");
            }
        }, "==B==").start();
    }
}
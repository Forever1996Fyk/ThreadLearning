package com.yk.concurrency.jcu.utils.condition;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: ThreadLearning
 * @description:  现在有两个线程, 线程A对一个数据自增, 线程B对数据进行打印, 但是如果这个数据没有自增, 线程B是不能打印的, 如果数据自增之前没有被打印, 那么线程A也就不能再自增
 *                类似于生产者消费者模型
 * @author: YuKai Fan
 * @create: 2020-10-22 22:02
 **/
public class ConditionExample1 {

    private final static ReentrantLock lock  = new ReentrantLock();

    private final static Condition condition = lock.newCondition();

    private static int data = 0;

    private static volatile boolean noUse = true;

    public static void main(String[] args) {
        new Thread(() -> {
            while (true) {
                build();
            }
        }).start();
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                while (true) {
                    userData();
                }
            }).start();
        }
    }

    private static void build() {
        try {
            lock.lock(); // 相当于synchronized
            // 没有使用, 就不能自增
            while (noUse) {
                condition.await(); // 相当于monitor.wait()
            }
            data++;
            Optional.of("P: " + data).ifPresent(System.out::println);
            TimeUnit.SECONDS.sleep(1);
            // 表示数据已经生产了, 还没有被消费, 所以通知线程B, 可以消费
            noUse = true;
            condition.signalAll();// 想当于monitor.notify()
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock(); // 相当于 release synchronized
        }
    }

    private static void userData() {
        try {
            lock.lock();
            // 如果数据被消费了, 被打印了
            while (!noUse) {
                condition.await();
            }
//            TimeUnit.SECONDS.sleep(1);
            Optional.of("C: " + data).ifPresent(System.out::println);

            // 数据已经被使用了, 通线程A可以继续生产
            noUse = false;
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
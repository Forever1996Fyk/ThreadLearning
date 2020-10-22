package com.yk.concurrency.jcu.utils.condition;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-22 22:22
 **/
public class ConditionExample2 {
    private final static ReentrantLock lock = new ReentrantLock(true);

    private final static Condition condition = lock.newCondition();

    private static int data = 0;

    private static volatile boolean noUse = true;

    /**
     * 问题分析:
     *
     * 1. 如果不使用 Condition, 仅仅使用Lock?
     * 问题1看起来是没有问题的，但是如果ReentrantLock()不设置为公平锁, 也就是不设置true,
     * 那么当生产者线程生成数据后把锁释放了, 但是下一次再去抢锁的时候, 跟消费者是同一个起跑线.也就是说, 即可能生产者抢到, 也可能消费者抢到, 这样就不满足我们的需求了。
     *
     * 2. 生产者线程得到锁并且进行await, 但是未跳出循环的时候,也就是在while中。 为什么消费者线程仍然可以得到锁, 进行下面的代码?
     * 当线程拿到锁, 在调用condition.await()时, 那么线程就会自动的释放锁, 同时当前线程也会放弃CPU的执行权, 直到被唤醒condition.signal(), 该线程才会去重新获取锁。
     *
     *
     * 3. 不使用lock, 仅仅使用Condition?
     * 如果只是用Condition, 那么会抛出异常。必须要在使用Condition前, 调用lock()。相当于wait(), notify(), notifyAll()必须要在synchronized中一样。
     *
     * @param args
     */
    public static void main(String[] args) {
        new Thread(() -> {
            while (true) {
                build();
            }
        }, "t1").start();

        new Thread(() -> {
            while (true) {
                userData();
            }
        }, "t2").start();
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
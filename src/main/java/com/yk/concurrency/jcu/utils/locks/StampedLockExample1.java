package com.yk.concurrency.jcu.utils.locks;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-24 11:09
 **/
public class StampedLockExample1 {

    /**
     * ReentrantLock 相对于 Synchronized:
     *
     * ReentrantLock的API更加灵活, 1.可以用声明式的获取锁, 释放锁。2.甚至可以声明这个锁多长时间可以抢得到。3.还可以查看有没有其他线程在队列中。
     * Synchronized的实现是黑盒的, 无法进行扩展，而且在性能上比不过ReentrantLock。
     * ReentrantLock的原理是自旋锁, 例如CAS, Synchronized是重锁
     *
     * ReentrantReadWriteLock 相对于 ReentrantLock:
     *
     * 在写操作时是需要加锁的, 但是在读操作是不需要加锁的。所以使用ReentrantReadWriteLock, 在多个线程进行读操作时，效率是很高的。
     *
     * 但是这样就会引起一些问题:
     *
     * 假如有100个线程在使用读写分离锁这样的机制, 但是其中有99个线程需要读操作, 1个线程需要写操作, 这样就会引起"写饥饿"情况。
     * 也就是说写操作一直抢不到锁, 更新很慢，但是查询很快。基于这种问题, 产生了StampedLock。
     *
     * StampedLock对读操作是乐观的, 但是这是相对于写操作而言。
     * 为什么这么说呢?
     * 比如说在进行读操作,拿到了读的锁, 那在写的时候是拿不到锁的, 那么这个读就是悲观的。
     * 但是如果在读的过程中, 发现允许写操作是可以进行抢锁的, 这样写操作就会改变一些数据，那么在读的过程中就会判断Stamped是否发生改变,
     * 如果Stamped发生改变, 那么就说明数据已经发生了改变, 那么读就不成功，读锁就会释放。如果Stamped还没有发生变化, 那读操作依然可以去读取数据。
     *
     * 简单的理解一下, 所谓悲观和乐观都是相对的。
     * 如果在读的过程中, 写操作是拿不到锁的, 那么读操作就是悲观的
     * 如果在读的过程中, 写操作依然可以抢锁, 那么读操作就是乐观的
     *
     *
     * 所以上面的就说明, StampedLock完全可以替换ReentranrLock, ReentranReadWriteLock。因为ReentranrLock, ReentranReadWriteLock的方法在StampedLock中都有。
     *
     * @param args
     */
    private final static StampedLock lock = new StampedLock();
    private final static List<Long> data = Lists.newArrayList();
    public static void main(String[] args) {
        final ExecutorService executorService  = Executors.newFixedThreadPool(100);
        Runnable readTask = () -> {
            while (true) {
                read();
            }
        };
        Runnable writeTask = () -> {
            while (true) {
                write();
            }
        };


        // 在这种情况下， 读操作还是悲观的, 如果有大量线程的读操作, 那么写操作很少有机会可能抢到锁。
        executorService.submit(readTask);
        executorService.submit(readTask);
        executorService.submit(readTask);
        executorService.submit(readTask);
        executorService.submit(readTask);
        executorService.submit(readTask);
        executorService.submit(readTask);
        executorService.submit(readTask);
        executorService.submit(writeTask);
        executorService.submit(writeTask);
    }

    private static void read() {
        long stamped = -1;
        try {
            stamped = lock.readLock();
            Optional.of(data.stream().map(String::valueOf).collect(Collectors.joining("#", "R-", ""))).ifPresent(System.out::println);
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlockRead(stamped);
        }
    }

    private static void write() {
        long stamp = -1;
        try {
            stamp = lock.writeLock();
            data.add(System.currentTimeMillis());
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlockWrite(stamp);
        }
    }
}
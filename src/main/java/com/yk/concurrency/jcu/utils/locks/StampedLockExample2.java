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
public class StampedLockExample2 {

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
        // 这是一个乐观锁, 而且不会被阻塞住
        long stamped = lock.tryOptimisticRead();

        // 判断stamped是否被修改, 如果被修改说明, 有写操作对数据进行修改, 那么读操作就拿不到锁
        // 这里要注意的是, 这里一定是 ! 不等于号。因为获取乐观锁的是不需要CAS操作的, 直接可以拿到状态判断, 并且返回stamp
        // 所以如果stamp验证失败false, 那么就表示有写操作, 那么就升级为悲观读锁readLock(), 所有读线程必须要在写线程释放写锁, 才能操作
        // 如果stamp验证成功true, 表示并没有写操作, 那么就可以直接操作数据, 不需要再获取悲观读锁readLock()操作, 多一步获取读锁的CAS的操作, 从而提高并发效率
        if (!lock.validate(stamped)) {
            stamped = lock.readLock();
            try {
                Optional.of(data.stream().map(String::valueOf).collect(Collectors.joining("#", "R-", ""))).ifPresent(System.out::println);
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlockRead(stamped);
            }
        } else {
            // 验证成功, 不需要获取悲观读锁, 直接打印
            try {
                Optional.of(data.stream().map(String::valueOf).collect(Collectors.joining("#", "R-", ""))).ifPresent(System.out::println);
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
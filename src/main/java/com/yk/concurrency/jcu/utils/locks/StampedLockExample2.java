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

        //判断stamped是否被修改, 如果被修改说明, 有写操作对数据进行修改, 那么读操作就拿不到锁
        if (lock.validate(stamped)) {
            stamped = lock.readLock();
            try {
                Optional.of(data.stream().map(String::valueOf).collect(Collectors.joining("#", "R-", ""))).ifPresent(System.out::println);
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlockRead(stamped);
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
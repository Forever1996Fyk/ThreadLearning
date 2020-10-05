package com.yk.concurrency.base.chapter10;

import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-05 21:18
 **/
public class LockTest {
    public static void main(String[] args) {
        final BooleanLock booleanLock = new BooleanLock();
        Stream.of("T1", "T2", "T3", "T4").forEach(name -> new Thread(() -> {
            try {
                booleanLock.lock(100L);
                Optional.of(Thread.currentThread().getName() + " have the lock Monitor").ifPresent(System.out::println);
                work();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                Optional.of(Thread.currentThread().getName() + " time out").ifPresent(System.out::println);
            } finally {
                booleanLock.unlock();
            }
        }, name).start());
    }

    private static void work() throws InterruptedException {
        Optional.of(Thread.currentThread().getName() + " is Working...").ifPresent(System.out::println);
        Thread.sleep(10_000);
    }
}
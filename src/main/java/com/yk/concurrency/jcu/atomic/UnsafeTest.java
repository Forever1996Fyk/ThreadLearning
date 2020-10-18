package com.yk.concurrency.jcu.atomic;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: ThreadLearning
 * @description: Unsafe是整个无锁编程的基础, 它涉及到系统系统的编码函数, c级别, c++级别， 汇编级别
 * @author: YuKai Fan
 * @create: 2020-10-18 21:29
 **/
public class UnsafeTest {

    public static void main(String[] args) throws Exception {
        // 这是无法直接调用的, 但是可以通过反射调用
//        Unsafe unsafe = Unsafe.getUnsafe();
//        System.out.println(unsafe);

//        Unsafe unsafe = getUnsafe();
//        System.out.println(unsafe);

        /**
         * StupidCounter:
         * Counter Result:9001475  result错误
         * Time passed in ms:182
         *
         * SyncCounter:
         * Counter Result:10000000 result正确
         * Time passed in ms:812   速度较慢
         *
         * LockCounter:
         * Counter Result:10000000 result正确
         * Time passed in ms:582   速度较快
         *
         * AtomicCounter:
         * Counter Result:10000000 result正确
         * Time passed in ms:287   速度较快
         */
        ExecutorService service = Executors.newFixedThreadPool(1000);
//        Counter counter = new StupidCounter();
//        Counter counter = new SyncCounter();
//        Counter counter = new LockCounter();
//        Counter counter = new AtomicCounter();
        Counter counter = new CasCounter();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            service.submit(new CounterRunnable(counter, 10000));
        }
        service.shutdown();
        service.awaitTermination(1, TimeUnit.HOURS);
        long end = System.currentTimeMillis();

        System.out.println("Counter Result:" + counter.getCounter());
        System.out.println("Time passed in ms:" + (end - start));
    }

    private static Unsafe getUnsafe() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            //允许访问私有属性
            f.setAccessible(true);
            return (Unsafe) f.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    interface Counter {
        void increment();

        long getCounter();
    }

    static class StupidCounter implements Counter {
        private long counter = 0;

        @Override
        public void increment() {
            counter++;
        }

        @Override
        public long getCounter() {
            return counter;
        }
    }

    static class SyncCounter implements Counter {
        private long counter = 0;

        @Override
        public synchronized void increment() {
            counter++;
        }

        @Override
        public long getCounter() {
            return counter;
        }
    }

    static class LockCounter implements Counter {
        private long counter = 0;

        private final Lock lock = new ReentrantLock();

        @Override
        public synchronized void increment() {
            try {
                lock.lock();
                counter++;
            } finally {
                lock.unlock();
            }
        }

        @Override
        public long getCounter() {
            return counter;
        }
    }

    static class AtomicCounter implements Counter {
        private AtomicLong counter = new AtomicLong();

        @Override
        public void increment() {
            counter.incrementAndGet();
        }

        @Override
        public long getCounter() {
            return counter.get();
        }
    }

    static class CasCounter implements Counter {
        private volatile long counter = 0;
        private Unsafe unsafe;
        private long offset;

        CasCounter() throws Exception {
            unsafe = getUnsafe();
            offset = unsafe.objectFieldOffset(CasCounter.class.getDeclaredField("counter"));
        }

        @Override
        public void increment() {
            long current = counter;
            long next = counter + 1;
            while (!unsafe.compareAndSwapLong(this, offset, current, next)) {
                current = next;
            }
        }

        @Override
        public long getCounter() {
            return counter;
        }
    }

    static class CounterRunnable implements Runnable {
        private final Counter counter;
        private final int num;

        public CounterRunnable(Counter counter, int num) {
            this.counter = counter;
            this.num = num;
        }

        @Override
        public void run() {
            for (int i = 0; i < num; i++) {
                counter.increment();
            }
        }
    }
}
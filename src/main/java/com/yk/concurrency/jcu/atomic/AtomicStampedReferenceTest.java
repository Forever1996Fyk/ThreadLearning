package com.yk.concurrency.jcu.atomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-18 21:00
 **/
public class AtomicStampedReferenceTest {

    /**
     * 设置初始值 和 版本号
     */
    private static AtomicStampedReference<Integer> atomicRef = new AtomicStampedReference<>(100, 0);

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                // 这样操作最终值, 还是100, 但是版本号为2
                boolean success = atomicRef.compareAndSet(100, 101, atomicRef.getStamp(), atomicRef.getStamp() + 1);
                System.out.println(success);

                boolean stampSuccess = atomicRef.compareAndSet(101, 100, atomicRef.getStamp(), atomicRef.getStamp() + 1);
                System.out.println(stampSuccess);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                int stamp = atomicRef.getStamp();
                System.out.println("Before sleep: stamp=" + stamp);
                TimeUnit.SECONDS.sleep(2);
                // 在进行compareAndSet时, 由于t1已经改过两次了, 所以这个版本号必须是3才会修改成功。
                boolean success = atomicRef.compareAndSet(100, 101, stamp, stamp + 1);
                System.out.println(success);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();
        t2.join();
        t2.join();
    }
}
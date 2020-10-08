package com.yk.concurrency.promote.chapter3;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-08 21:21
 **/
public class VolatileTest {
    /**
     * 如果不加volatile, reader就会进入死循环, 不会打印。
     * update线程修改 INIT_VALUE 之后, reader线程并没有感知到 INIT_VALUE 的变化, 所以localValue仍然为0
     *
     * 这部分内容就涉及到Java内存模型, 以及CPU相关问题
     *
     * 对于CPU内存而言, 分为主内存, 高速缓存。
     * 程序加载过程后, 数据会被加载到主内存和缓存。
     * 在多线程的环境下, 每个线程都只会在第一次在主内存中获取数据, 之后就会在缓存中拿数据。
     * 而对数据修改时, 会在缓存中修改数据后, 刷新到主内存中去。
     *
     * 例如: i = 1; i = i + 1 ===> 在CPU中操作流程是: 在主内存中读取i的数据, 然后把i放入缓存中, cpu执行i+1的指令, 然后把结果放入到缓存, 最后缓存在刷新到主内存。
     * 上面的操作在单线程中是没有问题的, 但是在多线程环境中就会出现问题, 当第二个线程执行上面的操作时, 线程操作在自己的缓存中i的值仍然是1, 这就导致了两个线程对i+1的操作结果是一致的。
     * 所以最后两个线程都操作i = i+1, 但是最终i的值仍然是2。
     *
     * i = 1;
     * i = i + 1;
     * 线程1: main memory -> i -> cache i+1 -> cache(2) -> main memory(2)
     * 线程2: main memory -> i -> cache i+1 -> cache(2) -> main memory(2)
     * i = 2;
     *
     * 那么解决上面的问题有两种方式:
     *
     * 1. 总线锁机制: 给数据总线加锁 [总线(数据总线, 地址总线, 控制总线)], 在cpu中就是LOCK#指令。一旦对数据总线加锁, 那么每次操作这个数据都只能有一个线程, 但是这就导致了多核CPU串行化了, 效率大大降低
     * 2. CPU高速缓存一致性协议: 多个高速缓存中的数据副本始终保持一致。
     * 它的核心思想就是: (1) 当CPU写入数据时, 如果发现该变量被共享(也就是说, 在其他CPU中也存在该变量的副本), 就会发出一个信号, 通知其他CPU该变量的缓存无效
     *
     * 但是READER线程为什么一直不会执行呢?
     *
     * 这就是因为Java内存的优化导致的。
     * Java内存在判断一段程序在线程中没有任何写操作时, JVM就会认为这段程序不会去主内存拿数据, 而去缓存中拿数据, 而线程READER在缓存中的数据没有任何变化,就导致了INIT_VALUE一直不会变化
     */
    private volatile static int INIT_VALUE = 0;

    private final static int MAX_LIMIT = 5;

    public static void main(String[] args) {
        new Thread(() -> {
            int localValue = INIT_VALUE;
            while (localValue < MAX_LIMIT) {
                if (localValue != INIT_VALUE) {
                    System.out.printf("The value update to [%d]\n", INIT_VALUE);
                    localValue = INIT_VALUE;
                }
            }

        }, "READER").start();

        new Thread(() -> {
            int localValue = INIT_VALUE;
            while (INIT_VALUE < MAX_LIMIT) {
                System.out.printf("Update the value to [%d]\n", ++localValue);
                INIT_VALUE = localValue;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "UPDATE").start();
    }
}
package com.yk.concurrency.jcu.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-15 21:10
 **/
public class AtomicIntegerDetailsTest {

    public static void main(String[] args) {
        getAndAdd();
    }

    public static void create() {
        AtomicInteger i = new AtomicInteger();
        System.out.println(i.get());
        i = new AtomicInteger(10);
        System.out.println(i.get());

        i.set(12);
        System.out.println(i.get());
        i.lazySet(13);
    }

    /**
     * 对于AtomicInteger中 getAndAdd() 方法, 它的基本原理是compareAndSet算法
     *
     * 在多线程环境下, 对一个值进行add操作, 并且获取，就会出现数据混乱问题。
     * 如果 i = 1; i = i + 1; 两个线程同时进行操作时, 可能出现的结果为 i = 3。
     * 因为当线程A去进行i+1操作的过程中, 可能线程B获得了cpu的执行权, 然后线程B去执行i+1操作, 线程A停止了，这就导致此时i的值为2了, 然后线程A再次以i=2的值进行 i+1 操作, 结果就为3
     *
     * getAndAdd() 源码:
     * for(;;) {
     *     int current = get();
     *     int next = current + delta;
     *     if (compareAndSet(current, next))
     *          return current;
     * }
     *
     * 解析: 在getAndAdd()方法中, i=1; i = i+1;
     *
     * 首先会获取到当前AtomicInteger的初始值value = 1, 作为current当前值即 current = 1, 然后进行+1操作, next = 1+1=2;
     *
     * 接着判断compareAndSet(current, next), 这个判断的核心是compareAndSwap算法。
     *
     * 也就是说, 当线程A进入在getAndAdd()方法时, 此时value=1, current=1, next=2, 如果此时线程B进入getAndAdd()方法时, 将value变成了2
     *
     * 然后再返回线程A在执行compareAndSet(current, next)时, 就会判断current,与value是否相等。如果不相等,就表示此时value已经被修改了, 于是就会判断false, 继续循环。
     * 在继续判断, 此时current=get()=2, next = 3, 再进行compareAndSet(current, next), 直到如果current与value相等, 将next赋给current, current=next， 最后返回current=3
     *
     * 所以compareAndSet(current, next)方法就是先判断current是否等于初始值value, 如果相等, 就像current与next交换,并返回true 如果不相等, 就直接返回false
     *
     * volatile修饰的变量, 能保证可见性, 有序性
     * CAS(CompareAndSwap)算法也就是CPU级别的同步指令, 相当于乐观锁, 它可以检测到其他线程对共享数据的变化情况。
     *
     * 但是上面的情况, 会带来一个严重的问题。ABA问题!!!
     *
     * 什么是ABA问题? 简单来说, 在CompareAndSwap(CAS)过程中, 其他线程对value经过很多次修改, 只是最终值与原值相同。
     *
     * 如果线程A对value值从1->2的过程中, 会去判断当前值与value是否相等, 即current==value? 如果在这个过程中, 有线程B把value从1改为2, 然后又改为1。
     * 对于线程A来说, 最终的值还是没有变化, 所以依旧会用这个值去处理。
     *
     * 解决办法也比较简单, 就是每次修改这个value值, 都给这个value加一个版本号, 最后判断这个版本号是不是最初, 如果不是就说明这个value被改过很多次, 那么CAS就失败
     * 实例: AtomicStampedReference中就实现了。(原子引用加戳)
     */
    public static void getAndAdd() {
        AtomicInteger value = new AtomicInteger(1);
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                int result = value.addAndGet(1);
                System.out.println(result);
            }
        }).start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                int result = value.addAndGet(1);
                System.out.println(result);
            }
        }).start();
    }
}
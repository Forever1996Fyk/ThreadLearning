package com.yk.concurrency.promote.chapter1;

/**
 * @program: ThreadLearning
 * @description: 懒汉式单例 加锁
 * @author: YuKai Fan
 * @create: 2020-10-07 21:34
 **/
public class LazyLockSingletonObject {
    private static LazyLockSingletonObject instance;

    private LazyLockSingletonObject() {

    }

    /**
     * 这种方式确实不存在线程安全问题, 但是加了synchronized, 就会导致每次调用getInstance()方法都只能进入一个线程, 而且返回实例实际上是一个读操作, 也就导致了性能上是有问题的。
     * @return
     */
    public synchronized static LazyLockSingletonObject getInstance() {
        if (instance == null) {
            instance = new LazyLockSingletonObject();
        }
        return LazyLockSingletonObject.instance;
    }
}
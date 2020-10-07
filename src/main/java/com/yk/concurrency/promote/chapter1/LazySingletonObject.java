package com.yk.concurrency.promote.chapter1;

/**
 * @program: ThreadLearning
 * @description: 这是最简单的懒加载单例模式, 当调用到LazySingletonObject对象时, 才会去创建唯一的实例。 也成为懒汉式单例
 *
 * @author: YuKai Fan
 * @create: 2020-10-07 21:34
 **/
public class LazySingletonObject {
    private static LazySingletonObject instance;

    private LazySingletonObject() {

    }

    /**
     * 存在线程安全问题
     *
     * 当有两个线程A, B同时调用getInstance()方法时, 线程A在进行instance = new LazySingletonObject()过程中, 此时线程B也进行if判断, 而instance此时其实仍然为null。
     *
     * 这就导致了, 在程序中产生不止一个LazySingletonObject实例
     *
     * @return
     */
    public static LazySingletonObject getInstance() {
        if (instance == null) {
            instance = new LazySingletonObject();
        }
        return LazySingletonObject.instance;
    }
}
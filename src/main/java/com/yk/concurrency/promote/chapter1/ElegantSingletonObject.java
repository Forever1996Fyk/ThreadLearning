package com.yk.concurrency.promote.chapter1;

/**
 * @program: ThreadLearning
 * @description: 优雅的单例模式: 这是比较优雅的单例模式, 在项目开发中也最好使用这种方式。
 *
 *               这种方式没有加锁, 也没有判断, 但是确实是线程安全的
 * @author: YuKai Fan
 * @create: 2020-10-07 22:08
 **/
public class ElegantSingletonObject {

    private ElegantSingletonObject() {

    }

    /**
     * InstanceHolder类是静态内部类, 只有在被主动调用的时候才会加载一次
     */
    private static class InstanceHolder {
        private final static ElegantSingletonObject instance = new ElegantSingletonObject();

    }

    /**
     * 当第一次调用这个方法时, 会主动加载 InstanceHolder静态内部类, 在初始化InstanceHolder时, 就会加载内部的静态实例ElegantSingletonObject, 而且只会被加载一次。
     * @return
     */
    public static ElegantSingletonObject getInstance() {
        return InstanceHolder.instance;
    }
}
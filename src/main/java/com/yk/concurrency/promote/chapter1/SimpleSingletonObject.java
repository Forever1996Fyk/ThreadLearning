package com.yk.concurrency.promote.chapter1;

/**
 * @program: ThreadLearning
 * @description: 这是最简单的单例模式, 也是线程安全的。但是这种方式存在问题。
 *               这种方式,在应用一启动就会new一个对象放在内存中, 如果很长时间都用不到这个实例, 就会占用内存资源。所以这种方式也成为饿汉式单例
 *
 *               所以可以进行懒加载, 这也是问题出现的原因。
 * @author: YuKai Fan
 * @create: 2020-10-07 21:30
 **/
public class SimpleSingletonObject {

    /**
     * 无法懒加载
     *
     * 当然如果这个类本身很小, 这种方式也是没问题的
     */
    private static final SimpleSingletonObject instance = new SimpleSingletonObject();

    private SimpleSingletonObject() {

    }

    public static SimpleSingletonObject getInstance() {
        return instance;
    }
}
package com.yk.concurrency.promote.chapter1;

/**
 * @program: ThreadLearning
 * @description: 双重检查单例模式，也是懒汉式单例。这种方式解决了加锁方式的懒汉式性能问题
 * @author: YuKai Fan
 * @create: 2020-10-07 21:43
 **/
public class DoubleCheckLazySingletonObject {
    private static DoubleCheckLazySingletonObject instance;

    private DoubleCheckLazySingletonObject() {

    }

    /**
     * double check。 但是即使如此还是存在一个很严重的问题。就是指令重排序
     *
     * 简单来说, 在初始化 instance = new DoubleCheckLazySingletonObject() 的过程中,
     * 可能对于instance已经指向DoubleCheckLazySingletonObject对象所在的堆内存地址了, 但是此时DoubleCheckLazySingletonObject并没有完成在堆内存的完全加载完成(这里面就存在JVM的一些调优和计算机的指令重排序问题)
     *
     * 这就导致了, 当线程A进入实例化过程时, 虽然没有完成最终的实例化结果, 但此时instance!=null了, 而线程B进行判断, 就会直接返回这个不是完整的实例。于是就产生了错误
     *
     *
     * @return
     */
    public static DoubleCheckLazySingletonObject getInstance() {
        if (instance == null) {
            synchronized (DoubleCheckLazySingletonObject.class) {
                if (instance == null) {
                    instance = new DoubleCheckLazySingletonObject();
                }
            }
        }

        return DoubleCheckLazySingletonObject.instance;
    }
}
package com.yk.concurrency.promote.chapter1;

/**
 * @program: ThreadLearning
 * @description: VDC表示 volatile Double Check
 * @author: YuKai Fan
 * @create: 2020-10-07 21:43
 **/
public class VDCLazySingletonObject {
    /**
     * volatile这个修饰词的目的就是明确的告诉编译器, 不要对这个对象进行优化, 也就是重排序。
     *
     * 也就是说对这个对象 读 的过程之前, 必须要对这个对象 写 的过程全部完结
     *
     * 这种方式可以解决下面的double check 产生的问题
     */
    private static volatile VDCLazySingletonObject instance;

    private VDCLazySingletonObject() {

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
    public static VDCLazySingletonObject getInstance() {
        if (instance == null) {
            synchronized (VDCLazySingletonObject.class) {
                if (instance == null) {
                    instance = new VDCLazySingletonObject();
                }
            }
        }

        return VDCLazySingletonObject.instance;
    }
}
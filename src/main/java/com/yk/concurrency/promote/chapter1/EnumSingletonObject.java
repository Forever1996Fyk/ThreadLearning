package com.yk.concurrency.promote.chapter1;

/**
 * @program: ThreadLearning
 * @description: java开发者推荐的方式: 枚举单例模式。但是写起来并不优雅
 * @author: YuKai Fan
 * @create: 2020-10-07 22:16
 **/
public class EnumSingletonObject {
    private EnumSingletonObject() {

    }

    private enum Singleton {
        INSTANCE;

        private final EnumSingletonObject instance;

        Singleton() {
            instance  = new EnumSingletonObject();
        }

        public EnumSingletonObject getInstance() {
            return instance;
        }
    }

    /**
     * 调用这个Singleton.INSTANCE时, 就会加载枚举类的构造方法, 然后创建EnumSingletonObject实例。
     *
     * 注意: 枚举类是线程安全的, 而且只会加载一次
     * @return
     */
    public static EnumSingletonObject getInstance() {
        return Singleton.INSTANCE.getInstance();
    }
}
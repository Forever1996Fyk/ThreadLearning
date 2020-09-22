package com.yk.concurrency.base.chapter1;

/**
 * @program: ThreadLearning
 * @description: Thread中使用的设计模式中的 模板方法模式: 比如我的算法基本上已经定下来了, 但是可能其中的逻辑可能是变化的, 所以可以把变化的逻辑抽象出去, 让子类实现。
 * @author: YuKai Fan
 * @create: 2020-09-22 22:48
 **/
public abstract class TemplateMethod {

    /**
     * 在正常模板方法中, 这个固定的算法一般是final的, 否则子类是可以覆盖这个方法的
     * @param message
     */
    public final void print(String message) {
        System.out.println("##############");
        wrapPrint(message);
        System.out.println("##############");
    }

    /**
     * 这个方法可以是抽象的, 也可以是空的实现
     * @param message
     */
    protected void wrapPrint(String message) {

    }

    public static void main(String[] args) {
        TemplateMethod t1 = new TemplateMethod() {
            @Override
            protected void wrapPrint(String message) {
                System.out.println("*" + message + "*");
            }
        };
        t1.print("Hello Thread");

        TemplateMethod t2 = new TemplateMethod() {
            @Override
            protected void wrapPrint(String message) {
                System.out.println("|" + message + "|");
            }
        };
        t2.print("Hello Thread");

    }
}
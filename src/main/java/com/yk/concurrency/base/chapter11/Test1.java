package com.yk.concurrency.base.chapter11;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-05 22:33
 **/
public class Test1 {
    private Test2 test2 = new Test2();

    public void test() {
        test2.test();
    }
}
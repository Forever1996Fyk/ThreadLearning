package com.yk.concurrency.promote.chapter8;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-11 10:50
 **/
public interface Future<T> {

    T get() throws InterruptedException;
}
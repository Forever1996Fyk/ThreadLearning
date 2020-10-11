package com.yk.concurrency.promote.chapter8;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-11 10:51
 **/
public interface FutureTask<T> {

    T call();
}
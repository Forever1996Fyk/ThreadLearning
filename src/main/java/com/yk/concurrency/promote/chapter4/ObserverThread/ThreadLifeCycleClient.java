package com.yk.concurrency.promote.chapter4.ObserverThread;

import java.util.Arrays;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-10 16:55
 **/
public class ThreadLifeCycleClient {

    public static void main(String[] args) {
        new ThreadLifeCycleObserver().concurrentQuery(Arrays.asList("1", "2"));
    }
}
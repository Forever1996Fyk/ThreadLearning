package com.yk.concurrency.base.chapter11;

import java.util.Arrays;
import java.util.Optional;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-05 22:34
 **/
public class Test2 {
    public void test() {
        // 可以获取到线程栈执行的具体信息
        Arrays.asList(Thread.currentThread().getStackTrace()).stream()
                .filter(e -> !e.isNativeMethod())
                .forEach(e -> Optional.of(e.getClassName() + ":" + e.getMethodName() + ":" + e.getLineNumber()).ifPresent(System.out::println));
    }
}
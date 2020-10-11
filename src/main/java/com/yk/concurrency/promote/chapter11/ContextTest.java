package com.yk.concurrency.promote.chapter11;

import java.util.stream.IntStream;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-11 14:43
 **/
public class ContextTest {
    public static void main(String[] args) {
        IntStream.range(1, 5)
                .forEach(i -> new Thread(new ExecutionTask()).start());
    }
}
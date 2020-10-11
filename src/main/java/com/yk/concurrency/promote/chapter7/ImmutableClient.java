package com.yk.concurrency.promote.chapter7;

import java.util.stream.IntStream;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-10 22:41
 **/
public class ImmutableClient {
    public static void main(String[] args) {
        //共享数据
        Person person = new Person("YK", "anhui");

        IntStream.rangeClosed(0, 5).forEach(i -> new UserPersonThread(person).start());
    }
}
package com.yk.concurrency.promote.chapter15;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-11 16:57
 **/
public class Message {

    private final String value;

    public Message(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
package com.yk.concurrency.promote.chapter13;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-11 16:03
 **/
public class Message {

    private String data;

    public Message(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
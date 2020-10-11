package com.yk.concurrency.promote.chapter15;

import java.util.stream.IntStream;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-11 17:01
 **/
public class PreThreadClient {

    public static void main(String[] args) {
        final MessageHandler handler = new MessageHandler();
        IntStream.rangeClosed(0, 10).forEach(i -> handler.request(new Message(String.valueOf(i))));

        handler.shutdown();
    }
}
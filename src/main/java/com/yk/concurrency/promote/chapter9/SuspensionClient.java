package com.yk.concurrency.promote.chapter9;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-11 12:00
 **/
public class SuspensionClient {
    public static void main(String[] args) throws InterruptedException {
        final RequestQueue queue = new RequestQueue();
        new ThreadClient(queue, "YK").start();
        ThreadServer threadServer = new ThreadServer(queue);
        threadServer.start();

        Thread.sleep(100000);
        threadServer.close();
    }
}
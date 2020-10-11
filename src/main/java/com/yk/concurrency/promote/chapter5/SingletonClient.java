package com.yk.concurrency.promote.chapter5;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-10 17:48
 **/
public class SingletonClient {
    public static void main(String[] args) {
        Gate gate = new Gate();
        User user1 = new User("bj", "beijing", gate);
        User user2 = new User("bs", "shanghai", gate);
        User user3 = new User("user3", "chuangzhou", gate);

        user1.start();
        user2.start();
        user3.start();
    }
}
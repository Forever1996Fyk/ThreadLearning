package com.yk.concurrency.promote.chapter16;

/**
 * @program: ThreadLearning
 * @description: 接受异步消息的主动方法
 * @author: YuKai Fan
 * @create: 2020-10-12 20:49
 **/
public interface ActiveObject {

    Result makeString(int count, char fillChar);

    void displayString(String text);
}
package com.yk.concurrency.promote.chapter4.ObserverDesginModel;

/**
 * @program: ThreadLearning
 * @description: 二进制
 * @author: YuKai Fan
 * @create: 2020-10-10 16:16
 **/
public class BinaryObserver extends Observer {

    public BinaryObserver(Subject subject) {
        super(subject);
    }
    @Override
    public void update() {
        System.out.println("Binary String:" + Integer.toBinaryString(subject.getState()));
    }
}
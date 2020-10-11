package com.yk.concurrency.promote.chapter4.ObserverDesginModel;

/**
 * @program: ThreadLearning
 * @description: 八进制
 * @author: YuKai Fan
 * @create: 2020-10-10 16:16
 **/
public class OctalObserver extends Observer {

    public OctalObserver(Subject subject) {
        super(subject);
    }

    @Override
    public void update() {
        System.out.println("Octal String:" + Integer.toOctalString(subject.getState()));
    }
}
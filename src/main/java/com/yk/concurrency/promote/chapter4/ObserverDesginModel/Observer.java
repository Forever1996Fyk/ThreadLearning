package com.yk.concurrency.promote.chapter4.ObserverDesginModel;

/**
 * @program: ThreadLearning
 * @description: 在Java.util包下也有一个Observer,只不过是接口类型
 * @author: YuKai Fan
 * @create: 2020-10-10 16:13
 **/
public abstract class Observer {

    /**
     * 订阅者, 事件源
     */
    protected Subject subject;

    public Observer(Subject subject) {
        this.subject = subject;
        subject.attach(this);
    }

    public abstract void update();
}
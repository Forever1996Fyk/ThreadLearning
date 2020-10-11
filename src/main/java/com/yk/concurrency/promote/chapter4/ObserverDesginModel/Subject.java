package com.yk.concurrency.promote.chapter4.ObserverDesginModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: ThreadLearning
 * @description: 观察者模式 --- 订阅, 即事件源
 * @author: YuKai Fan
 * @create: 2020-10-10 16:09
 **/
public class Subject {

    private List<Observer> observers  = new ArrayList<>();

    /**
     * 每次改变的状态
     */
    private int state;

    public int getState() {
        return state;
    }

    /**
     * 状态发生变化
     * @param state
     */
    public void setState(int state) {
        // 注意这里, 第一次赋值state时, 是不会通知的, 也就是第一次进行setState(xx)时, 此时是state == this.state是true
        if (state == this.state) {
            return;
        }
        this.state = state;
        notifyAllObserver();
    }

    /**
     * 附加每一个观察者observer
     * @param observer
     */
    public void attach(Observer observer) {
        observers.add(observer);
    }

    /**
     * 通知所有的观察者observer
     */
    private void notifyAllObserver() {
        observers.stream().forEach(Observer::update);
    }
 }
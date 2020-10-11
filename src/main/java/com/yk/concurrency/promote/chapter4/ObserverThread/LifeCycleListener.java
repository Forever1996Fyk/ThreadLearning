package com.yk.concurrency.promote.chapter4.ObserverThread;

/**
 * @program: ThreadLearning
 * @description: 生命周期监听器
 * @author: YuKai Fan
 * @create: 2020-10-10 16:29
 **/
public interface LifeCycleListener {
    void onEvent(ObserverRunnable.RunnableEvent event);
}
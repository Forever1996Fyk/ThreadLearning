package com.yk.concurrency.promote.chapter4.ObserverThread;

import java.util.List;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-10 16:31
 **/
public class ThreadLifeCycleObserver implements LifeCycleListener {
    private final Object LOCK = new Object();

    public void concurrentQuery(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        ids.stream().forEach(id -> new Thread(new ObserverRunnable(this) {
            @Override
            public void run() {
                try {
                    // 每个线程执行方法前, 通知Observer监听器, 打印状态
                    notifyChange(new RunnableEvent(RunnableState.RUNNING, Thread.currentThread(), null));
                    System.out.println("query for the id " + id);
                    Thread.sleep(1000L);
                    int x = 1/0;
                    // 每个线程执行方法完之后, 通知Observer监听器, 打印状态
                    notifyChange(new RunnableEvent(RunnableState.DONE, Thread.currentThread(), null));
                } catch (Exception e) {
                    // 每个线程执行方法异常时, 通知Observer监听器, 打印状态
                    notifyChange(new RunnableEvent(RunnableState.ERROR, Thread.currentThread(), e));
                }
            }
        }, id).start());
    }

    @Override
    public void onEvent(ObserverRunnable.RunnableEvent event) {
        synchronized (LOCK) {
            System.out.println("The runnable [" + event.getThread().getName() + "] data changed and state is [" + event.getState() + "]");
            if (event.getThrowable() != null) {
                System.out.println("The runnable [" + event.getThread().getName() + "] process failed.");
                event.getThrowable().printStackTrace();
            }
        }
    }
}
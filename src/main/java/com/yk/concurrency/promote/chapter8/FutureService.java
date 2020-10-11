package com.yk.concurrency.promote.chapter8;

import java.util.function.Consumer;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-11 10:52
 **/
public class FutureService {

    public <T> Future<T> submit(final FutureTask<T> task) {
        AsynFuture<T> asynFuture = new AsynFuture<>();
        new Thread(() -> {
            T result = task.call();
            asynFuture.done(result);
        }).start();
        return asynFuture;
    }

    /**
     * 加入consumer, 回调的方式。可以主动的返回数据, 而不需要去get()
     * @param task
     * @param consumer
     * @param <T>
     * @return
     */
    public <T> Future<T> submit(final FutureTask<T> task, final Consumer<T> consumer) {
        AsynFuture<T> asynFuture = new AsynFuture<>();
        new Thread(() -> {
            T result = task.call();
            asynFuture.done(result);
            consumer.accept(result);
        }).start();
        return asynFuture;
    }

}
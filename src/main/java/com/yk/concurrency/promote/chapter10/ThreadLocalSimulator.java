package com.yk.concurrency.promote.chapter10;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-11 14:21
 **/
public class ThreadLocalSimulator<T> {

    private final Map<Thread, T> storage = new HashMap<>();

    public void set(T t) {
        synchronized (this) {
            Thread key = Thread.currentThread();
            storage.put(key, t);
        }
    }

    public T get() {
        synchronized (this) {
            Thread key = Thread.currentThread();
            T value = storage.get(key);
            if (value == null) {
                return initialValue();
            }
            return value;
        }
    }

    public T initialValue() {
        return null;
    }
}
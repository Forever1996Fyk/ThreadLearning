package com.yk.concurrency.jcu.atomic;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-15 22:16
 **/
public class GetLockException extends RuntimeException {

    public GetLockException(String msg) {
        super(msg);
    }
}
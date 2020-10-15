package com.yk.concurrency.promote.chapter16;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-12 20:56
 **/
public class RealResult implements Result {
    private final Object resultValue;

    public RealResult(Object resultValue) {
        this.resultValue = resultValue;
    }

    @Override
    public Object getResultValue() {
        return this.resultValue;
    }
}
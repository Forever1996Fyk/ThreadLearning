package com.yk.juc.atomic;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-17 16:45
 **/
public class AtomicBooleanTest {

    @Test
    public void testCreateWithNoArguments() {
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        System.out.println(atomicBoolean.get());
    }

    /**
     * compareAndSet(), 比较并交换。
     *
     * 判断当前是否是初始值, 如果是就设为另一个值, 如果不是就失败
     */
    @Test
    public void  testCompareAndSet() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        boolean b = atomicBoolean.compareAndSet(true, false);
        System.out.println(b);
        System.out.println(atomicBoolean.get());
    }
}
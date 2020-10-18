package com.yk.juc.atomic;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-17 17:09
 **/
public class AtomicLongTest {

    /**
     * 在AtomicLong中有一个 static final boolean VM_SUPPORTS_LONG_CAS = VMSupportsCS8();
     * 这是直接给JVM调用的，在AtomicInteger中是没有的。
     *
     * 因为long类型的数据时64位的，而在long类型数据进行传递时，是先拿高位(high 32), 后拿低位(low 32)。
     * 而这个操作就不是原子性了。
     */
    @Test
    public void testCreate() {
        AtomicLong atomicLong = new AtomicLong(100L);
        System.out.println(atomicLong.get());

    }
}
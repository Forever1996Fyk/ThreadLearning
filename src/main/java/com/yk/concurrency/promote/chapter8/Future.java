package com.yk.concurrency.promote.chapter8;

/**
 * @program: ThreadLearning
 * @description: Future设计模式, 使用场景,
 *               如果一个任务分为 work1, work2, work3。work3会用到work1的结果, 但是work1可能执行的方法很长, 导致work2一直处于等待状态, 那么work1可以使用future,
 *               这样work1,work2可以并行, 当执行到work3时, 可以从work1的future中获取到相应的结果
 *
 *               线程池中的Future使用
 *               {@link com.yk.concurrency.jcu.utils.executors.FutureExample1}
 * @author: YuKai Fan
 * @create: 2020-10-11 10:50
 **/
public interface Future<T> {

    T get() throws InterruptedException;
}
package com.yk.concurrency.promote.chapter10;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-11 14:08
 **/
public class ThreadLocalSimpleTest {
    private static ThreadLocal<String> threadLocal = new ThreadLocal() {
        @Override
        protected Object initialValue() {
            return "YK";
        }
    };

    /**
     * JVM start Main Thread
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
//        threadLocal.set("YK");
        Thread.sleep(1000);
        System.out.println(threadLocal.get());
    }
}
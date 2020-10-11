package com.yk.concurrency.promote.chapter4.ObserverDesginModel;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-10 16:20
 **/
public class ObserverClient {
    public static void main(String[] args) {
        final Subject subject = new Subject();
        new BinaryObserver(subject);
        new OctalObserver(subject);

        System.out.println("=====================");
        subject.setState(10);
        System.out.println("=====================");
        subject.setState(10);
        System.out.println("=====================");
        subject.setState(15);
        System.out.println("=====================");
    }
}
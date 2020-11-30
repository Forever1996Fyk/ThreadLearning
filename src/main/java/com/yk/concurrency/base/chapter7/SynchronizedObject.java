package com.yk.concurrency.base.chapter7;


import org.openjdk.jol.info.ClassLayout;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-11-18 14:18
 **/
public class SynchronizedObject {

    public static void main(String[] args) {
        Object o = new Object();

        System.out.println(ClassLayout.parseInstance(o).toPrintable());
    }
}
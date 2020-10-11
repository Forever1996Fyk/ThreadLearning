package com.yk.concurrency.promote.chapter7;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-10 22:40
 **/
public class UserPersonThread extends Thread {
    private Person person;


    public UserPersonThread(Person person) {
        this.person = person;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " print " + person.toString());
    }
}
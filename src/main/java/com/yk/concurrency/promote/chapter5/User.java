package com.yk.concurrency.promote.chapter5;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-10 17:45
 **/
public class User extends Thread{
    private final String name;

    private final String address;

    private final Gate gate;

    public User(String name, String address, Gate gate) {
        this.name = name;
        this.address = address;
        this.gate = gate;
    }

    @Override
    public void run() {
        System.out.println(name + " BEGIN ");
        while (true) {
            this.gate.pass(name, address);
        }
    }
}
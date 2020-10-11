package com.yk.concurrency.promote.chapter7;

/**
 * @program: ThreadLearning
 * @description: 不可变对象一定是线程安全的, 因为只有在定义该对象的时候才能够把数据传入, 其他情况下是无法改变数据
 * @author: YuKai Fan
 * @create: 2020-10-10 22:37
 **/
public final class Person {
    private final String name;
    private final String address;

    public Person(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
package com.yk.concurrency.jcu.atomic;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-17 17:28
 **/
public class AtomicReferenceTest {
    public static void main(String[] args) {
        AtomicReference<Simple> atomic = new AtomicReference<>(new Simple("YK", 24));
        System.out.println(atomic.get());

        boolean b = atomic.compareAndSet(new Simple("YK", 22), new Simple("YK", 20));
        System.out.println(b);

    }

    static class Simple {
        private String name;
        private int age;

        public Simple(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }
}
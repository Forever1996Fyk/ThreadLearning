package com.yk.concurrency.jcu.utils.cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-11-10 15:20
 **/
public class test {

    static class User {
        private String name;

        public User(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException, TimeoutException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        String s = get(cyclicBarrier);


        User user = new User(s);

        cyclicBarrier.await(10, TimeUnit.SECONDS);
        System.out.println(user.getName());
    }

    public static String get(CyclicBarrier cyclicBarrier) {
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
                int i = 1/0;
                cyclicBarrier.await(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }).start();
        return "测试";
    }
}
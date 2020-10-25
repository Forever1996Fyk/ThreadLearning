package com.yk.concurrency.jcu.utils.executors;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-25 11:57
 **/
public class test {

    public static void main(String[] args) {
        ExecutorService executorService = new ThreadPoolExecutor(10, 20, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10), r -> {
            Thread thread = new Thread(r);
            return thread;
        }, new ThreadPoolExecutor.AbortPolicy());

        /**
         * 批量添加用户, 添加用户的同时还需要初始化分配用户角色
         * 也就是先要addUser(), 然后addUserRole()操作。
         * 这两个操作肯定是要在同一个事务里，如果addUser异常, 那addUserRole()肯定不会执行, 进行回滚了
         */
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                // 这个两个操作是原子性的，都在一个线程中, 所以肯定拿到的是同一个数据库连接, 也是同一个事务
                //addUser();
                //addUserRole();
            });
        }
    }
}
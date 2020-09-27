package com.yk.concurrency.base.chapter6;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-09-27 22:05
 **/
public class ThreadCloseForce1 {

    public static void main(String[] args) {
        ThreadService service = new ThreadService();
        long start = System.currentTimeMillis();
        service.execute(() -> {
            // load a very heavy resource.
            while (true) {

            }

            // 如果执行线程任务, 只用了5000ms就结束了, 那么守护线程也不会等待10000ms才会结束, 也会直接结束。
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        });
        service.shutdown(10000);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
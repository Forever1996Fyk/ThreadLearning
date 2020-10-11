package com.yk.concurrency.promote.chapter8;

/**
 * @program: ThreadLearning
 * @description: 同步调用
 * @author: YuKai Fan
 * @create: 2020-10-11 10:47
 **/
public class SyncInvoker {


    /**
     * Future         -> 代表是未来的一个凭据
     * FutureTask     -> 将你的调用逻辑进行隔离
     * FutureService  -> 桥接 Future和FutureTask
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        /*// 如果是同步调用中, 必须要等待10s之后, 才能返回结果
        String result = get();
        System.out.println(result);*/
        FutureService futureService = new FutureService();
//        Future<String> future = futureService.submit(() -> {
//            try {
//                Thread.sleep(10000L);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return "finish";
//        });

        System.out.println("=============");
        System.out.println("do other thing.");
        Thread.sleep(1000);
        System.out.println("=============");

        // 但是使用这种方式, 还是需要主动的去获取数据,如果这个线程早就处理完了, 不应该要主动的获取, 而是通过一种方式, 当数据处理完成时, 直接返回。
        // System.out.println(future.get());

        futureService.submit(() -> {
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "finish";
        }, System.out::println);
    }

    /**
     * 假设这是一个非常耗时的操作
     * @return
     * @throws InterruptedException
     */
    public static String get() throws InterruptedException {
        Thread.sleep(10000L);
        return "finish";
    }
}
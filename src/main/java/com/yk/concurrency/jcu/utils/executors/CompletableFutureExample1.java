package com.yk.concurrency.jcu.utils.executors;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-29 20:55
 **/
public class CompletableFutureExample1 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        completableFuture();
//        futureDoTask();
        completableFutureDoTask();
    }

    /**
     * 普通Future
     * 缺点:
     *
     * 1. 如果是一批Future执行, 那么是无法知道那个先结束,
     * 2. Future执行完成, 需要主动去拿, 而且会blocked
     * 3. Future执行完, get出来的结果, 如果再想提交一个callable或者runnable, 那么就必须要在定义一个, 无法达到级联的效果
     */
    private static void future() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        Future<?> future = executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        /**
         * 之前学习过Future我们知道, 虽然Future可以异步的获取结果, 但是这个结果我们仍然需要通过get()方法, 才能获取到。同时这也是个同步方法。
         *
         * 那么有没有方法可以当futureTask执行结束后, 主动的通知。注册一个回调, 执行结束之后, 主动的回调。
         *
         * 这就是CompletableFuture, 它其实是Future和ExecutorService的结合
         */
        while (!future.isDone()) {

        }

        System.out.println("DONE");
    }

    /**
     * CompletableFuture
     */
    private static void completableFuture() throws InterruptedException {
        /**
         * 这段代码会发现, 并不会输出DONE。
         *
         * CompletableFuture是ExecutorService和Future的结合体, 它内部有一个内置的Executor, 但是CompletableFuture并没有办法管理ExecutorService,
         * 因为它处理的是一个Future, 而Future并没有跟ExecutorService有直接的关系。
         *
         * 因此CompletableFuture内置的ExecutorService中所有线程都是一个守护线程, 也就是说在这个例子中如果main线程结束了, 那么Executor中的线程也就结束了。
         *
         * 当我们join main线程时, 就会发现, 打印出DONE
         */
        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).whenComplete((v, t) -> System.out.println("DONE"));
        System.out.println("==========I am not blocked============");

        Thread.currentThread().join();
    }

    /**
     * 1. 定义10个Callable, 每个Callable都要去获取一个值, 假设获取的时间较长,
     * 2. 利用ExecutorService中的invokeAll, 能够获取10个Future, 并且拿到这个值, 去处理任务, 假设处理任务的时间也较长
     *
     * 上面步骤很像, 在实际开发中, 我们可能有多个任务需要从数据库中获取数据, 然后再把这个数据进行处理。如果获取数据和处理数据都需要很长的一段时间,
     * 利用Future, 必须要先等待所有的数据都获取到了之后, 然后在统一去处理。(为什么不能拿出来一个数据, 就去处理一个数据。原因就在get()方法, 这个方法时阻塞的, 必须要等待所有的结果都拿出来, 才能执行之后的任务)
     */
    private static void futureDoTask() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Callable<Integer>> tasks = IntStream.range(0, 10).boxed()
                .map(i -> (Callable<Integer>) () -> get()).collect(Collectors.toList());

        executorService.invokeAll(tasks).stream().map(future -> {
            try {
                return future.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).parallel().forEach(CompletableFutureExample1::display);
    }

    /**
     * 利用CompletableFuture, 我们获取一条数据, 就会处理一条数据, 效率大大提高。
     *
     * @throws InterruptedException
     */
    private static void completableFutureDoTask() throws InterruptedException {
        IntStream.range(0, 10).boxed()
                .forEach(i ->
                        CompletableFuture.supplyAsync(CompletableFutureExample1::get)
                                .thenAccept(CompletableFutureExample1::display)
                                .whenComplete((v, t) -> System.out.println(i + "Done"))
                );

        Thread.currentThread().join();
    }

    private static void display(int data) {
        int value = ThreadLocalRandom.current().nextInt(20);
        try {
            System.out.println(Thread.currentThread().getName() + " display will be sleep " + value);
            TimeUnit.SECONDS.sleep(value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " display execute done " + data);
    }

    private static int get() {
        int value = ThreadLocalRandom.current().nextInt(20);
        try {
            System.out.println(Thread.currentThread().getName() + " will be sleep " + value);
            TimeUnit.SECONDS.sleep(value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " execute done " + value);
        return value;
    }
}
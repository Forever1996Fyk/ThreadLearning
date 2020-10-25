package com.yk.concurrency.jcu.utils.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

/**
 * @program: ThreadLearning
 * @description: 分而治之ForkJoin。可以将task分为task1,task2分别去处理, 然后返回最终的结果。而在task1,task2又可以分为多个子任务分别处理...
 *
 *               task -> task1, task2 -> task1.1 task1.2 task2.1 task2.2 -> ....
 * @author: YuKai Fan
 * @create: 2020-10-24 14:54
 **/
public class ForkJoinRecursiveTask {

    private final static int MAX_THRESHOLD = 3;

    public static void main(String[] args) {
        final ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Long> future = forkJoinPool.submit(new CalculatedRecursiveTask(0, 10000000));
        try {
            long result = future.get();
            System.out.println(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static class CalculatedRecursiveTask extends RecursiveTask<Long> {

        private final int start;

        private final int end;

        CalculatedRecursiveTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            if (end - start <= MAX_THRESHOLD){
                return LongStream.rangeClosed(start, end).sum();
            } else {
                int middle = (start + end) / 2;
                CalculatedRecursiveTask leftTask = new CalculatedRecursiveTask(start, middle);
                CalculatedRecursiveTask rightTask = new CalculatedRecursiveTask(middle + 1, end);

                leftTask.fork();
                rightTask.fork();

                return leftTask.join() + rightTask.join();
            }
        }
    }
}
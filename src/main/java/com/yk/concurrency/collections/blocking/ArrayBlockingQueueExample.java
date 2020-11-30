package com.yk.concurrency.collections.blocking;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Time;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-11-04 20:45
 **/
public class ArrayBlockingQueueExample {

    /**
     * 1. 有边界的 FIFO Queue
     * @param size
     * @param <T>
     * @return
     */
    public <T>ArrayBlockingQueue<T> create(int size) {
        return new ArrayBlockingQueue<>(size);
    }

    /**
     * add()添加元素时, 如果没有超过边界, 就会立即加入队列 并返回true; 如果超过边界, 就会抛出异常
     */
    @Test
    public void testAddMethodNotExceedCapacity() {
        ArrayBlockingQueue<String> queue = create(5);
        Assert.assertThat(queue.add("Hello1"), CoreMatchers.equalTo(true));
        Assert.assertThat(queue.add("Hello2"), CoreMatchers.equalTo(true));
        Assert.assertThat(queue.add("Hello3"), CoreMatchers.equalTo(true));
        Assert.assertThat(queue.add("Hello4"), CoreMatchers.equalTo(true));
        Assert.assertThat(queue.add("Hello5"), CoreMatchers.equalTo(true));
        Assert.assertThat(queue.size(), CoreMatchers.equalTo(5));
    }

    /**
     * add()添加元素时, 如果没有超过边界, 就会立即加入队列 并返回true; 如果超过边界, 就会抛出 IllegalStateException 异常
     */
    @Test(expected = IllegalStateException.class)
    public void testAddMethodExceedCapacity() {
        ArrayBlockingQueue<String> queue = create(5);
        Assert.assertThat(queue.add("Hello1"), CoreMatchers.equalTo(true));
        Assert.assertThat(queue.add("Hello2"), CoreMatchers.equalTo(true));
        Assert.assertThat(queue.add("Hello3"), CoreMatchers.equalTo(true));
        Assert.assertThat(queue.add("Hello4"), CoreMatchers.equalTo(true));
        Assert.assertThat(queue.add("Hello5"), CoreMatchers.equalTo(true));
        Assert.assertThat(queue.add("Hello6"), CoreMatchers.equalTo(true));
        Assert.fail("should not process to here");
    }

    /**
     * offer()添加元素时, 如果没有超过边界, 就会立即加入队列 并返回true; 如果超过边界, 就会返回false
     */
    @Test
    public void testOfferMethodNotExceedCapacity() {
        ArrayBlockingQueue<String> queue = create(5);
        Assert.assertThat(queue.offer("Hello1"), CoreMatchers.equalTo(true));
        Assert.assertThat(queue.offer("Hello2"), CoreMatchers.equalTo(true));
        Assert.assertThat(queue.offer("Hello3"), CoreMatchers.equalTo(true));
        Assert.assertThat(queue.offer("Hello4"), CoreMatchers.equalTo(true));
        Assert.assertThat(queue.offer("Hello5"), CoreMatchers.equalTo(true));
    }

    /**
     * offer()添加元素时, 如果没有超过边界, 就会立即加入队列; 如果超过边界, 就会返回false
     */
    @Test
    public void testOfferMethodExceedCapacity() {
        ArrayBlockingQueue<String> queue = create(5);
        Assert.assertThat(queue.offer("Hello1"), CoreMatchers.equalTo(true));
        Assert.assertThat(queue.offer("Hello2"), CoreMatchers.equalTo(true));
        Assert.assertThat(queue.offer("Hello3"), CoreMatchers.equalTo(true));
        Assert.assertThat(queue.offer("Hello4"), CoreMatchers.equalTo(true));
        Assert.assertThat(queue.offer("Hello5"), CoreMatchers.equalTo(true));
        Assert.assertThat(queue.offer("Hello6"), CoreMatchers.equalTo(false));
    }

    /**
     * put()添加元素时, 如果没有超过边界, 就会立即加入队列; 如果超过边界, 当前线程就会blocked
     */
    @Test
    public void testPutMethodNotExceedCapacity() throws InterruptedException {
        ArrayBlockingQueue<String> queue = create(5);
        queue.put("Hello1");
        queue.put("Hello2");
        queue.put("Hello3");
        queue.put("Hello4");
        queue.put("Hello5");
        Assert.assertThat(queue.size(), CoreMatchers.equalTo(5));
    }

    /**
     * put()添加元素时, 如果没有超过边界, 就会立即加入队列; 如果超过边界, 当前线程就会blocked
     */
    @Test
    public void testPutMethodExceedCapacity() throws InterruptedException {
        ArrayBlockingQueue<String> queue = create(5);
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        executorService.schedule(() -> {
            try {
                Assert.assertThat(queue.take(), CoreMatchers.equalTo("Hello1"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, TimeUnit.SECONDS);

        queue.put("Hello1");
        queue.put("Hello2");
        queue.put("Hello3");
        queue.put("Hello4");
        queue.put("Hello5");

        // 当线程执行到此处时, 就会blocked
        queue.put("Hello6");

        TimeUnit.SECONDS.sleep(3);
        executorService.shutdown();
    }
}
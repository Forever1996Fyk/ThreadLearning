package com.yk.concurrency.base.chapter7;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-09-28 20:35
 **/
public class SynchronizedRunnable implements Runnable {
    private int index = 1;

    private final static int MAX = 500;

    private final Object MONITOR = new Object();

    /**
     * 同步方法 没有指定对象时, 此时拿到锁的对象是 this --> SynchronizedRunnable的实例。
     *
     * 这就会产生多个线程去抢这个this锁, 但是此时一个线程 Thread0 抢到了方法锁, 并且执行方法中的代码, Thread0 就直接处理完里面的所有逻辑, 并且释放锁。
     *
     * 而 Thread1 在拿到锁时, 此时index已经是500了, 也就直接break。后面的线程也是如此。
     *
     * 也就是说 方法锁, 所对应的对象是this, 有可能导致, 只有一个线程在工作, 其他线程在等待锁并且拿到的时候, 又什么事都没做。这样就不合理了
     */
//    @Override
//    public synchronized void run() {
//        while (true) {
//            if (index > MAX)
//                break;
//            try {
//                Thread.sleep(5);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println(Thread.currentThread() + " 的号码是:" + (index++));
//        }
//    }

    @Override
    public void run() {
        while (true) {
            if (ticket())
                break;
        }
    }

    /**
     * 为什么要同步代码块?
     *
     * 因为我们有共享变量, 在 多个线程 改变共享变量时, 就会产生线程安全问题。
     *
     * @return
     */
    private synchronized boolean ticket() {
        // getField, 这是个读操作, 并不会改变index的值
        if (index > MAX)
            return true;
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /**
         * index++ => index = index +1
         *
         * 1. get field index
         *
         * 2. index = index + 1
         *
         * 3. put field
         */
        System.out.println(Thread.currentThread() + " 的号码是:" + (index++));
        return false;
    }
}
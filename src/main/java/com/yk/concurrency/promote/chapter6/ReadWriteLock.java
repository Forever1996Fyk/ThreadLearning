package com.yk.concurrency.promote.chapter6;

/**
 * @program: ThreadLearning
 * @description: 读写锁
 * @author: YuKai Fan
 * @create: 2020-10-10 21:52
 **/
public class ReadWriteLock {

    /**
     * 当前有多少线程进行读操作
     */
    private int readingReaders = 0;

    /**
     * 线程想读操作, 但是无法读取的线程个数
     */
    private int waitingReaders = 0;

    /**
     * 当前有多少线程在进行写操作, 肯定只有一个线程
     */
    private int writingWriters = 0;

    /**
     * 线程想写操作, 但是无法写入的线程个数
     */
    private int waitingWriters = 0;

    /**
     * 是否更偏向写操作
     */
    public boolean preferWriter = true;

    public ReadWriteLock() {
        this(true);
    }

    public ReadWriteLock(boolean preferWriter) {
        this.preferWriter = preferWriter;
    }

    public synchronized void readLock() throws InterruptedException {
        this.waitingReaders++;
        try {
            // 如果当前有线程在进行写操作, 就不能读取
            while (writingWriters > 0 || (preferWriter && waitingWriters > 0)) {
                this.wait();
            }
            //
            this.readingReaders++;
        } finally {
            this.waitingReaders--;
        }
    }

    public synchronized void readUnlock() {
        this.readingReaders--;
        this.notifyAll();
    }

    public synchronized void writeLock() throws InterruptedException {
        this.waitingWriters++;
        try {
            while (readingReaders > 0 || writingWriters > 0) {
                this.wait();
            }
            this.writingWriters++;
        } finally {
            this.waitingWriters--;
        }
    }

    public synchronized void writeUnlock() {
        this.writingWriters--;
        this.notifyAll();
    }
}
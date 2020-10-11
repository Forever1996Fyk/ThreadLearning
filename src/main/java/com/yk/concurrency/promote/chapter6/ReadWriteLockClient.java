package com.yk.concurrency.promote.chapter6;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-10 22:21
 **/
public class ReadWriteLockClient {
    public static void main(String[] args) {
        final SharedData data = new SharedData(10);
        new ReaderWorker(data).start();
        new ReaderWorker(data).start();
        new ReaderWorker(data).start();
        new ReaderWorker(data).start();
        new ReaderWorker(data).start();

        new WriterWorker(data, "qwertyuipasdnsdn").start();
        new WriterWorker(data, "QWERTYUIPASDNSDN").start();
    }
}
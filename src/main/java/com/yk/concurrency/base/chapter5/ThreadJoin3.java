package com.yk.concurrency.base.chapter5;

/**
 * @program: ThreadLearning
 * @description: 案例: 多台机器去采集数据, 并保存到数据库。我们想要获取采集过程所花费的时间。
 * @author: YuKai Fan
 * @create: 2020-09-27 20:45
 **/
public class ThreadJoin3 {
    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        Thread t1 = new Thread(new CaptureRunnable("M1", 10000L));
        Thread t2 = new Thread(new CaptureRunnable("M2", 30000L));
        Thread t3 = new Thread(new CaptureRunnable("M3", 15000L));

        t1.start();
        t2.start();
        t3.start();

        /**
         * 在不用join的情况下, main线程在t1,t2,t3还没执行完, 就保存了开始结束时间, 这样很明显是不符合我们的需求的, 所以这时候就可以使用join。
         *
         * 在t1,t2,t3都执行完的情况下, 在保存时间。
         */
        t1.join();
        t2.join();
        t3.join();

        long endTime = System.currentTimeMillis();
        System.out.printf("save data begin timestamp is: %s, end timestamp is : %s \n", startTime, endTime);
    }
}

class CaptureRunnable implements Runnable {
    private String machineName;

    private long spendTime;

    public CaptureRunnable(String machineName, long spendTime) {
        this.machineName = machineName;
        this.spendTime = spendTime;
    }

    @Override
    public void run() {
        // do the really capture data
        try {
            Thread.sleep(spendTime);
            System.out.printf(machineName + " completed data capture at timestamp [%s] and successful \n", System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getResult() {
        return machineName + " finish.";
    }
}
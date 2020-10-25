package com.yk.concurrency.jcu.utils.executors;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-25 16:00
 **/
public class TimeScheduler {

    /**
     * 定时任务 解决任务
     *
     * Timer/TimerTask
     * SchedulerExecutorService
     * crontab
     * cron4j
     * quartz
     *
     * @param args
     */
    public static void main(String[] args) {
        Timer timer = new Timer();
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("======" + System.currentTimeMillis());
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        // 但是timer有一个问题, 如果设置定时时间为1s, 但是任务执行时间是10s, timerTask就不会1s之后执行一次, 而是10s执行一次。这样执行间隔时间就不准了
        timer.schedule(task, 1000, 1000);
    }
}
package com.yk.concurrency.base.chapter6;

/**
 * @program: ThreadLearning
 * @description: 自定义线程执行与强制关闭
 * @author: YuKai Fan
 * @create: 2020-09-27 21:56
 **/
public class ThreadService {

    private Thread executeThread;

    private boolean finished = false;

    /**
     * 思路: 可以定义runner线程, 把这个runner线程设成执行线程的守护线程。如果执行线程中断, 生命周期结束, 那么守护线程也会结束了。
     *
     * 整体的代码逻辑如下:
     *
     * 1. 我们定义一个全局的执行线程 executeThread 和一个结束标识 finished = false。在execute(Runnable task)方法中, 传入Runnable。
     *
     * 2. 实例化executeThread线程, 并实现run()方法, 在run方法中, 定义一个runner线程, 传入task任务接口。并且将这个线程设置为executeThread的守护线程。
     * (这样一旦这个executeThread关闭了, 那这个守护线程也就关闭了, 也就是这个task任务也就关闭了)
     *
     * 3. 然后启动runner(注意启动完runner，一定要join()), 并且将 finished设为true。
     *
     * 4. 在启动executeThread线程。
     *
     * 5. 创建shutdown(long mills)方法, 该方法是判断任务执行时间是否超过定义的时间, 如果超过就interrupt executeThread线程, 如此守护线程 task 也就关闭了。
     *
     * @param task
     */
    public void execute(Runnable task) {
        executeThread = new Thread() {
            @Override
            public void run() {
                Thread runner = new Thread(task);
                runner.setDaemon(true);

                runner.start();
                try {
                    // 必须要join, 否则这个守护线程, 可能在main线程执行完之后, 还没有启动。这样就没有意义了
                    runner.join();
                    finished = true;
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }
        };

        // 一定先执行 executeThread.start() 才会调用run方法, 才会执行 runner.start();
        executeThread.start();
    }

    /**
     *  这里要注意程序执行的逻辑。
     *
     *  当main线程调用上面的execute方法时, 直到执行到runner.join()方法时, task线程才会启动。当task线程启动后, main线程才开始执行后面的代码(因为用了join())。
     *
     *  也就是说当执行到runner.join()方法时, Task线程开始运行了, 但是此时还没有把finished设为true, 就直接执行main线程的shutdown()方法了。
     *
     *  此时, finished = false, main线程就会进入while循环, 判断这个时间是否超过指定的时间。
     *
     *  如果超过了时间就中断executeThread, 如果在指定的时间内, task线程执行完了, 那么此时就会执行finished = true, 这样就会跳出while循环。
     *
     * @param mills
     */
    public void shutdown(long mills) {
        long currentTime = System.currentTimeMillis();
        while (!finished) {
            if ((System.currentTimeMillis() - currentTime) >= mills) {
                System.out.println("任务超时, 需要结束!");
                executeThread.interrupt();
                break;
            }

            try {
                executeThread.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("执行线程被打断!");
                break;
            }
        }

        finished = false;
    }
}
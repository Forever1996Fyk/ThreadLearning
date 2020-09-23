package com.yk.concurrency.base.chapter2;

/**
 * @program: ThreadLearning
 * @description: 多线程方式模拟银行排队叫号
 * @author: YuKai Fan
 * @create: 2020-09-23 20:12
 **/
public class TicketWindow extends Thread{

    // 银行柜台名称
    private final String name;

    // 最大号码
    private final int MAX = 50;

    /**
     * 初始号码
     *
     * 使用static修饰, 为了让线程共享同一个index变量, 但是无法保证index顺序问题
     *
     * 但是使用static的生命周期很长, 它是伴随着JVM的启动销毁, 因为static修饰的变量不是在栈内存中, 而是有独立的空间, 所以即使实例销毁了, 但是static变量依然存在
     */
    private static int index = 1;

    public TicketWindow(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        while (index <= MAX) {
            System.out.println("柜台" + name + ", 当前的号码是:" + (index++));
        }
    }
}
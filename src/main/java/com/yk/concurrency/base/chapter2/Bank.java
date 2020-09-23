package com.yk.concurrency.base.chapter2;

/**
 * @program: ThreadLearning
 * @description: 银行柜台
 * @author: YuKai Fan
 * @create: 2020-09-23 20:14
 **/
public class Bank {

    public static void main(String[] args) {

        TicketWindow ticketWindow1 = new TicketWindow("一号柜台");
        ticketWindow1.start();

        TicketWindow ticketWindow2 = new TicketWindow("二号柜台");
        ticketWindow2.start();

        TicketWindow ticketWindow3 = new TicketWindow("三号柜台");
        ticketWindow3.start();
    }
}
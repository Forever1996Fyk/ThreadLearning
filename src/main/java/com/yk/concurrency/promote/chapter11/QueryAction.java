package com.yk.concurrency.promote.chapter11;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-11 14:34
 **/
public class QueryAction {

    public void execute(Context context) {
        try {
            Thread.sleep(1000L);
            String name = "YK " + Thread.currentThread().getName();
            context.setName(name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void execute() {
        try {
            Thread.sleep(1000L);
            String name = "YK " + Thread.currentThread().getName();
            ActionContext.getInstance().getContext().setName(name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
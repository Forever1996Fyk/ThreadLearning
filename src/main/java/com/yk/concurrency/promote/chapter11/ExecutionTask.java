package com.yk.concurrency.promote.chapter11;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-11 14:33
 **/
public class ExecutionTask implements Runnable {
    private QueryAction queryAction = new QueryAction();

    private QueryFromHttpAction httpAction = new QueryFromHttpAction();

    @Override
    public void run() {
//        final Context context = new Context();
//        queryAction.execute(context);
//        System.out.println("The Name query successful");
//
//        httpAction.execute(context);
//        System.out.println("The CardId query successful");
//
//        System.out.println("The Name is :" + context.getName() + " and CardId is : " + context.getCardId());

        queryAction.execute();
        System.out.println("The Name query successful");

        httpAction.execute();
        System.out.println("The CardId query successful");

        Context context = ActionContext.getInstance().getContext();
        System.out.println("The Name is :" + context.getName() + " and CardId is : " + context.getCardId());
    }
}
package com.yk.concurrency.promote.chapter11;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-11 14:38
 **/
public class QueryFromHttpAction {

    public void execute(Context context) {
        String name = context.getName();
        String cardId = getCardId(name);
        context.setCardId(cardId);
    }

    public void execute() {
        Context context = ActionContext.getInstance().getContext();
        String cardId = getCardId(context.getName());
        context.setCardId(cardId);
    }

    private String getCardId(String name) {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "232481312312" + Thread.currentThread().getId();
    }
}
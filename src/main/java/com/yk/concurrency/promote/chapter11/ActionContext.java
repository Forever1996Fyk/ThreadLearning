package com.yk.concurrency.promote.chapter11;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-11 14:52
 **/
public class ActionContext {
    private static final ThreadLocal<Context> THREAD_LOCAL = new ThreadLocal<Context>() {
        @Override
        protected Context initialValue() {
            return new Context();
        }
    };

    private static class ContextHolder {
        private final static ActionContext actionContext = new ActionContext();
    }

    public static ActionContext getInstance() {
        return ContextHolder.actionContext;
    }

    public Context getContext() {
        return THREAD_LOCAL.get();
    }
}
package com.yk.concurrency.promote.chapter4.ObserverThread;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-10 16:26
 **/
public abstract class ObserverRunnable implements Runnable {
    final protected LifeCycleListener listener;

    public ObserverRunnable(LifeCycleListener listener) {
        this.listener = listener;
    }

    protected void notifyChange(final RunnableEvent event) {
        listener.onEvent(event);
    }

    public enum RunnableState {
        RUNNING, ERROR, DONE;
    }

    public static class RunnableEvent {
        private final RunnableState state;
        private final Thread thread;
        private final Throwable throwable;

        public RunnableEvent(RunnableState state, Thread thread, Throwable throwable) {
            this.state = state;
            this.thread = thread;
            this.throwable = throwable;
        }

        public RunnableState getState() {
            return state;
        }

        public Thread getThread() {
            return thread;
        }

        public Throwable getThrowable() {
            return throwable;
        }
    }
}
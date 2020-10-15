package com.yk.concurrency.promote.chapter16;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-10-12 21:13
 **/
public class DisplayStringRequest extends MethodRequest {

    private final String text;

    public DisplayStringRequest(Servant servant, final String text) {
        super(servant, null);
        this.text = text;
    }

    @Override
    public void execute() {
        this.servant.displayString(this.text);
    }
}
package com.yk.concurrency.promote.chapter16;

/**
 * @program: ThreadLearning
 * @description: 负责生产  Result makeString(int count, char fillChar); 对象
 * @author: YuKai Fan
 * @create: 2020-10-12 21:05
 **/
public class MakeStringRequest extends MethodRequest {

    private final int count;
    private final char fillChar;

    public MakeStringRequest(Servant servant, FutureResult futureResult, int count, char fillChar) {
        super(servant, futureResult);
        this.count = count;
        this.fillChar = fillChar;
    }

    @Override
    public void execute() {
        Result result = servant.makeString(count, fillChar);
        futureResult.setResult(result);
    }
}
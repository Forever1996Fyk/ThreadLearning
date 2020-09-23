package com.yk.concurrency.base.chapter2.designModel;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-09-23 20:50
 **/
public class SimpleCalculatorStrategy implements CalculatorStrategy {
    public double calculator(double salary, double bonus) {
        return salary * 0.1 + bonus * 0.15;
    }
}
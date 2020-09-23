package com.yk.concurrency.base.chapter2.designModel;

/**
 * @program: ThreadLearning
 * @description: 税务计算策略类
 * @author: YuKai Fan
 * @create: 2020-09-23 20:47
 **/
@FunctionalInterface
public interface CalculatorStrategy {

    double calculator(double salary, double bonus);
}
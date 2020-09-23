package com.yk.concurrency.base.chapter2.designModel;

/**
 * @program: ThreadLearning
 * @description: 使用策略模式, 使用每个策略接口的实现类, 传入具体的计算类, 进行计算
 * @author: YuKai Fan
 * @create: 2020-09-23 20:51
 **/
public class TaxCalculatorMain2 {

    public static void main(String[] args) {
        TaxCalculator taxCalculator = new TaxCalculator(10000d, 2000d);
        CalculatorStrategy calculatorStrategy = new SimpleCalculatorStrategy();
        taxCalculator.setCalculatorStrategy(calculatorStrategy);

//        taxCalculator.setCalculatorStrategy((s, b) -> s * 0.1 + b * 0.15); // 也可以使用lambda表达式
        System.out.println(taxCalculator.calculator());
    }
}
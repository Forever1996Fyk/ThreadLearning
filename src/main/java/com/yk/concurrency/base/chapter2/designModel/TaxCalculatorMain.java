package com.yk.concurrency.base.chapter2.designModel;

/**
 * @program: ThreadLearning
 * @description:
 * @author: YuKai Fan
 * @create: 2020-09-23 20:42
 **/
public class TaxCalculatorMain {

    public static void main(String[] args) {
        TaxCalculator calculator = new TaxCalculator(10000d, 2000d) {
            @Override
            public double calcTax() {
                return getSalary() * 0.1 + getBonus() * 0.15; // 如果需要改变税率的值, 此时就需要改变代码
            }
        };
        double tax = calculator.calculator();
        System.out.println(tax);
    }
}
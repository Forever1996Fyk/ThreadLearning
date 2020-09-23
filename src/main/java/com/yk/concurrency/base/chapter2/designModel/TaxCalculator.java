package com.yk.concurrency.base.chapter2.designModel;

/**
 * @program: ThreadLearning
 * @description: 利用这个税务计算器举例, 模拟Thread类所用到的设计模式。
 * @author: YuKai Fan
 * @create: 2020-09-23 20:36
 **/
public class TaxCalculator {

    /**
     * 工资
     */
    private final double salary;

    /**
     * 奖金
     */
    private final double bonus;

    /**
     * 将这个税务计算的策略注入到税务计算类中
     */
    private CalculatorStrategy calculatorStrategy;

    /**
     *  由于这个国家每年对税务的计算方式都需要调整, 如果设计不好就需要改变代码可能会导致一些问题
     * @param salary
     * @param bonus
     */
    public TaxCalculator(double salary, double bonus) {
        this.salary = salary;
        this.bonus = bonus;
    }

    protected double calcTax() {
//        return 0.0d;
        // 调用策略接口的方法, 每个接口的实现都会有它自己的具体方法
        return calculatorStrategy.calculator(salary, bonus);
    }

    public double calculator() {
        return this.calcTax();
    }

    public double getSalary() {
        return salary;
    }

    public double getBonus() {
        return bonus;
    }

    public void setCalculatorStrategy(CalculatorStrategy calculatorStrategy) {
        this.calculatorStrategy = calculatorStrategy;
    }
}
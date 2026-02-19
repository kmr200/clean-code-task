package org.example.task2.model;

import lombok.Getter;

@Getter
public class SalariedEmployee extends Employee {

    private static final double MONTHS_IN_YEAR = 12.0;
    private static final double BONUS_RATE = 0.15;

    private final Money annualSalary;

    public SalariedEmployee(String name, Money annualSalary) {
        super(name);
        this.annualSalary = annualSalary;
    }

    /**
     * Pay = annual salary / 12.
     */
    @Override
    public Money calculatePay() {
        return annualSalary.multiply(1.0 / MONTHS_IN_YEAR);
    }

    /**
     * Bonus = 15% of monthly pay.
     */
    @Override
    public Money calculateBonus() {
        return calculatePay().multiply(BONUS_RATE);
    }
}

package org.example.task2.model;

import lombok.Getter;

@Getter
public class CommissionedEmployee extends Employee {

    private static final double BONUS_RATE = 0.10;

    private final Money baseSalary;
    private final double commissionRate;
    private final Money totalSales;

    public CommissionedEmployee(String name, Money baseSalary,
                                double commissionRate, Money totalSales) {
        super(name);
        this.baseSalary = baseSalary;
        this.commissionRate = commissionRate;
        this.totalSales = totalSales;
    }

    /**
     * Pay = base salary + (commission rate * total sales).
     */
    @Override
    public Money calculatePay() {
        return baseSalary.add(totalSales.multiply(commissionRate));
    }

    /**
     * Bonus = 10% of total commission earned.
     */
    @Override
    public Money calculateBonus() {
        return totalSales.multiply(commissionRate).multiply(BONUS_RATE);
    }
}

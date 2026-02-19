package org.example.task2.model;

import lombok.Getter;

@Getter
public class HourlyEmployee extends Employee {

    private static final double OVERTIME_THRESHOLD = 40.0;
    private static final double OVERTIME_RATE = 1.5;
    private static final double BONUS_RATE = 0.05;

    private final Money hourlyRate;
    private final double hoursWorked;

    public HourlyEmployee(String name, Money hourlyRate, double hoursWorked) {
        super(name);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }

    /**
     * Pay = regular hours * rate + overtime hours * rate * 1.5.
     */
    @Override
    public Money calculatePay() {
        if (hoursWorked <= OVERTIME_THRESHOLD) {
            return hourlyRate.multiply(hoursWorked);
        }
        Money regularPay = hourlyRate.multiply(OVERTIME_THRESHOLD);
        Money overtimePay = hourlyRate.multiply((hoursWorked - OVERTIME_THRESHOLD) * OVERTIME_RATE);
        return regularPay.add(overtimePay);
    }

    /**
     * Bonus = 5% of total pay.
     */
    @Override
    public Money calculateBonus() {
        return calculatePay().multiply(BONUS_RATE);
    }
}

package org.example.task1.fine.impl;

import org.example.task1.fine.FineStrategy;

public class DailyFineStrategy implements FineStrategy {
    private final double dailyRate;

    public DailyFineStrategy(double dailyRate) {
        this.dailyRate = dailyRate;
    }

    @Override
    public double calculate(long daysOverdue) {
        return daysOverdue * dailyRate;
    }
}

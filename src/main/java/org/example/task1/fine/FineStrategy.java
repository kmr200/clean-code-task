package org.example.task1.fine;

/**
 * Strategy interface for calculating overdue fines.
 */
public interface FineStrategy {
    double calculate(long daysOverdue);
}

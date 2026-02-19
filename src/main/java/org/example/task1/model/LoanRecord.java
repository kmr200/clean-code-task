package org.example.task1.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@AllArgsConstructor
public class LoanRecord {

    private final String bookId;
    private final String userId;
    private final LocalDate checkoutDate;

    public boolean isOverdue(long maxLoanDays) {
        return LocalDate.now().isAfter(checkoutDate.plusDays(maxLoanDays));
    }

    public long daysOverdue(long maxLoanDays) {
        long overdue = ChronoUnit.DAYS.between(checkoutDate.plusDays(maxLoanDays), LocalDate.now());
        return Math.max(0, overdue);
    }
}

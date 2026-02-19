package org.example.task1.event.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.task1.event.LibraryEventListener;
import org.example.task1.model.LoanRecord;
import org.example.task1.model.Reservation;

/**
 * Simple console/log implementation of the event listener.
 */
@Slf4j
public class LoggingEventListener implements LibraryEventListener {

    @Override
    public void onBookCheckedOut(LoanRecord loan) {
        log.info("Book [{}] checked out by user [{}] on {}",
                loan.getBookId(), loan.getUserId(), loan.getCheckoutDate());
    }

    @Override
    public void onBookReturned(String bookId, String userId) {
        log.info("Book [{}] returned by user [{}]", bookId, userId);
    }

    @Override
    public void onBookReserved(Reservation reservation) {
        log.info("Book [{}] reserved by user [{}]",
                reservation.getBookId(), reservation.getUserId());
    }

    @Override
    public void onFineIssued(String userId, String bookId, double fineAmount) {
        log.warn("Fine of ${} issued to user [{}] for overdue book [{}]",
                fineAmount, userId, bookId);
    }
}

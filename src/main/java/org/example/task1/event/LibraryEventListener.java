package org.example.task1.event;

import org.example.task1.model.LoanRecord;
import org.example.task1.model.Reservation;

public interface LibraryEventListener {
    void onBookCheckedOut(LoanRecord loan);
    void onBookReturned(String bookId, String userId);
    void onBookReserved(Reservation reservation);
    void onFineIssued(String userId, String bookId, double fineAmount);
}

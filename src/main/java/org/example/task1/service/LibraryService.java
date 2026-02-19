package org.example.task1.service;

import lombok.extern.slf4j.Slf4j;
import org.example.task1.event.LibraryEventListener;
import org.example.task1.exception.BookNotCheckedOutException;
import org.example.task1.exception.BookNotFoundException;
import org.example.task1.fine.FineStrategy;
import org.example.task1.model.Book;
import org.example.task1.model.LoanRecord;
import org.example.task1.model.Reservation;
import org.example.task1.repository.BookRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Core library service. Handles checkout, return, reservation, and overdue logic.
 * Notifies registered listeners on all significant events.
 */
@Slf4j
public class LibraryService {

    private static final long MAX_LOAN_DAYS = 14;

    private final BookRepository repository;
    private final FineStrategy fineStrategy;
    private final List<LibraryEventListener> listeners = new CopyOnWriteArrayList<>();

    public LibraryService(BookRepository repository, FineStrategy fineStrategy) {
        this.repository = repository;
        this.fineStrategy = fineStrategy;
    }

    public void registerListener(LibraryEventListener listener) {
        listeners.add(listener);
    }

    /**
     * Checks out a book to a user. If the book is already loaned, creates a reservation instead.
     *
     * @param bookId the ID of the book
     * @param userId the ID of the user
     * @throws BookNotFoundException if the book doesn't exist in the system
     */
    public synchronized void checkOutOrReserve(String bookId, String userId)
            throws BookNotFoundException {

        Book book = repository.findBook(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        if (repository.isCheckedOut(bookId)) {
            Reservation reservation = new Reservation(bookId, userId, LocalDate.now());
            repository.addReservation(reservation);
            listeners.forEach(l -> l.onBookReserved(reservation));
        } else {
            LoanRecord loan = new LoanRecord(bookId, userId, LocalDate.now());
            repository.saveLoan(loan);
            listeners.forEach(l -> l.onBookCheckedOut(loan));
        }
    }

    /**
     * Returns a book. Calculates and issues any applicable fine.
     * Automatically assigns the book to the next user in the reservation queue.
     *
     * @param bookId the ID of the book being returned
     * @throws BookNotFoundException    if the book doesn't exist
     * @throws BookNotCheckedOutException if the book wasn't checked out
     */
    public synchronized void returnBook(String bookId)
            throws BookNotFoundException, BookNotCheckedOutException {

        repository.findBook(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        LoanRecord loan = repository.findLoan(bookId)
                .orElseThrow(() -> new BookNotCheckedOutException(bookId));

        repository.removeLoan(bookId);
        listeners.forEach(l -> l.onBookReturned(bookId, loan.getUserId()));

        issueFineIfOverdue(loan);
        assignToNextReservation(bookId);
    }

    /**
     * Scans all active loans and issues fines for any that are overdue.
     * Intended to be called by a scheduled job.
     */
    public void processOverdueBooks() {
        repository.getAllLoans().stream()
                .filter(loan -> loan.isOverdue(MAX_LOAN_DAYS))
                .forEach(this::issueFineIfOverdue);
    }

    private void issueFineIfOverdue(LoanRecord loan) {
        long daysOverdue = loan.daysOverdue(MAX_LOAN_DAYS);
        if (daysOverdue > 0) {
            double fine = fineStrategy.calculate(daysOverdue);
            listeners.forEach(l -> l.onFineIssued(loan.getUserId(), loan.getBookId(), fine));
        }
    }

    private void assignToNextReservation(String bookId) {
        repository.pollNextReservation(bookId).ifPresent(reservation -> {
            LoanRecord newLoan = new LoanRecord(bookId, reservation.getUserId(), LocalDate.now());
            repository.saveLoan(newLoan);
            listeners.forEach(l -> l.onBookCheckedOut(newLoan));
            log.info("Book [{}] auto-assigned to next reservation holder [{}]",
                    bookId, reservation.getUserId());
        });
    }
}

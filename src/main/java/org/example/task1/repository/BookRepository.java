package org.example.task1.repository;

import org.example.task1.model.Book;
import org.example.task1.model.LoanRecord;
import org.example.task1.model.Reservation;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BookRepository {

    private final Map<String, Book> books = new ConcurrentHashMap<>();
    private final Map<String, LoanRecord> activeLoans = new ConcurrentHashMap<>();
    // Queue preserves reservation order (FIFO) per book
    private final Map<String, Queue<Reservation>> reservations = new ConcurrentHashMap<>();

    public void addBook(Book book) {
        books.put(book.getBookId(), book);
    }

    public Optional<Book> findBook(String bookId) {
        return Optional.ofNullable(books.get(bookId));
    }

    public boolean isCheckedOut(String bookId) {
        return activeLoans.containsKey(bookId);
    }

    public void saveLoan(LoanRecord record) {
        activeLoans.put(record.getBookId(), record);
    }

    public Optional<LoanRecord> findLoan(String bookId) {
        return Optional.ofNullable(activeLoans.get(bookId));
    }

    public void removeLoan(String bookId) {
        activeLoans.remove(bookId);
    }

    public void addReservation(Reservation reservation) {
        reservations
                .computeIfAbsent(reservation.getBookId(), k -> new ConcurrentLinkedQueue<>())
                .add(reservation);
    }

    public Optional<Reservation> pollNextReservation(String bookId) {
        Queue<Reservation> queue = reservations.get(bookId);
        return (queue != null) ? Optional.ofNullable(queue.poll()) : Optional.empty();
    }

    public Collection<LoanRecord> getAllLoans() {
        return Collections.unmodifiableCollection(activeLoans.values());
    }
}

package org.example.task1.exception;

public class BookNotCheckedOutException extends Exception {
    public BookNotCheckedOutException(String bookId) {
        super("Book is not currently checked out: " + bookId);
    }
}

package org.example.task1.exception;

public class BookUnavailableException extends Exception {
    public BookUnavailableException(String bookId) {
        super("Book is currently checked out: " + bookId);
    }
}
package org.example.task1.exception;

public class BookNotFoundException extends Exception {
    public BookNotFoundException(String bookId) {
        super("Book not found: " + bookId);
    }
}
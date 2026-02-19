package org.example.task1;

import java.util.HashMap;

public class LibrarySystem {
    private HashMap<String, String> bookRegistry = new HashMap<>();

    public void checkOutBook(String bookId, String userId) {
        if (bookRegistry.containsKey(bookId)) {
            System.out.println("Book is currently unavailable.");
        } else {
            bookRegistry.put(bookId, userId);
            System.out.println("Book checked out to " + userId);
        }
    }

    public void returnBook(String bookId) {
        if (bookRegistry.containsKey(bookId)) {
            bookRegistry.remove(bookId);
            System.out.println("Book returned.");
        } else {
            System.out.println("This book was not checked out.");
        }
    }

    public static void main(String[] args) {
        LibrarySystem library = new LibrarySystem();
        library.checkOutBook("BK001", "USR001");
        library.returnBook("BK001");
    }
}

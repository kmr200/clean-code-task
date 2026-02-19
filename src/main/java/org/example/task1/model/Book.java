package org.example.task1.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Book {
    private final String bookId;
    private final String title;
    private final String author;
}

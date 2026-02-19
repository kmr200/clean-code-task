package org.example.task1.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Reservation {
    private final String bookId;
    private final String userId;
    private final LocalDate reservationDate;
}

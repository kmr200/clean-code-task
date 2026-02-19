package org.example.task2.exception;

public class InvalidEmployeeType extends RuntimeException {
    public InvalidEmployeeType(String message) {
        super(message);
    }
}

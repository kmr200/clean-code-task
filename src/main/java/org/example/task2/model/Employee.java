package org.example.task2.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class Employee {

    private final String name;

    public abstract Money calculatePay();

    public abstract Money calculateBonus();
}

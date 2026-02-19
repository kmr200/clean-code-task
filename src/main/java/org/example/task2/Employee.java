package org.example.task2;

import org.example.task2.exception.InvalidEmployeeType;

public class Employee {
    private String name;
    private String type;

    public Money calculatePay(Employee e) throws InvalidEmployeeType {
        switch (e.type) {
            case "COMMISSIONED":
                return calculateCommissionedPay(e);
            case "HOURLY":
                return calculateHourlyPay(e);
            case "SALARIED":
                return calculateSalariedPay(e);
            default:
                throw new InvalidEmployeeType(e.type);
        }
    }

    public Money calculateBonus(Employee e) throws InvalidEmployeeType {
        switch (e.type) {
            case "COMMISSIONED":
                return calculateCommissionedBonus(e);
            case "HOURLY":
                return calculateHourlyBonus(e);
            case "SALARIED":
                return calculateSalariedBonus(e);
            default:
                throw new InvalidEmployeeType(e.type);
        }
    }
}


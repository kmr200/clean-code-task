package org.example.task2.factory;

import org.example.task2.model.CommissionedEmployee;
import org.example.task2.model.HourlyEmployee;
import org.example.task2.model.Money;
import org.example.task2.model.SalariedEmployee;

public class EmployeeFactory {

    public static CommissionedEmployee commissioned(String name, Money baseSalary,
                                                    double commissionRate, Money totalSales) {
        return new CommissionedEmployee(name, baseSalary, commissionRate, totalSales);
    }

    public static HourlyEmployee hourly(String name, Money hourlyRate, double hoursWorked) {
        return new HourlyEmployee(name, hourlyRate, hoursWorked);
    }

    public static SalariedEmployee salaried(String name, Money annualSalary) {
        return new SalariedEmployee(name, annualSalary);
    }
}

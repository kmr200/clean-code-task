package task2;

import org.example.task2.factory.EmployeeFactory;
import org.example.task2.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeTest {

    @Test
    void commissioned_calculatePay_returnsBasePlusCommission() {
        // base $1000 + 10% of $5000 sales = $1500
        CommissionedEmployee emp = EmployeeFactory.commissioned(
                "Alice", Money.of(1000), 0.10, Money.of(5000));

        assertThat(emp.calculatePay().getAmount())
                .isEqualByComparingTo("1500.00");
    }


    @Test
    void commissioned_calculateBonus_returns10PercentOfCommission() {
        // commission = $500, bonus = 10% of $500 = $50
        CommissionedEmployee emp = EmployeeFactory.commissioned(
                "Alice", Money.of(1000), 0.10, Money.of(5000));

        assertThat(emp.calculateBonus().getAmount())
                .isEqualByComparingTo("50.00");
    }

    @Test
    void commissioned_zeroSales_payEqualsBaseSalary() {
        CommissionedEmployee emp = EmployeeFactory.commissioned(
                "Alice", Money.of(1000), 0.10, Money.of(0));

        assertThat(emp.calculatePay().getAmount())
                .isEqualByComparingTo("1000.00");
    }
    @Test
    void hourly_calculatePay_noOvertime() {
        // 40h * $20 = $800
        HourlyEmployee emp = EmployeeFactory.hourly("Bob", Money.of(20), 40);

        assertThat(emp.calculatePay().getAmount())
                .isEqualByComparingTo("800.00");
    }

    @Test
    void hourly_calculatePay_withOvertime() {
        // 40h * $20 = $800 + 5h * $20 * 1.5 = $150 → $950
        HourlyEmployee emp = EmployeeFactory.hourly("Bob", Money.of(20), 45);

        assertThat(emp.calculatePay().getAmount())
                .isEqualByComparingTo("950.00");
    }

    @Test
    void hourly_calculateBonus_is5PercentOfPay() {
        // pay = $800, bonus = 5% = $40
        HourlyEmployee emp = EmployeeFactory.hourly("Bob", Money.of(20), 40);

        assertThat(emp.calculateBonus().getAmount())
                .isEqualByComparingTo("40.00");
    }

    @Test
    void hourly_zeroHours_payIsZero() {
        HourlyEmployee emp = EmployeeFactory.hourly("Bob", Money.of(20), 0);

        assertThat(emp.calculatePay().getAmount())
                .isEqualByComparingTo("0.00");
    }

    @Test
    void salaried_calculatePay_returnsMonthlySlice() {
        // $60,000 / 12 = $5,000
        SalariedEmployee emp = EmployeeFactory.salaried("Carol", Money.of(60_000));

        assertThat(emp.calculatePay().getAmount())
                .isEqualByComparingTo("5000.00");
    }

    @Test
    void salaried_calculateBonus_is15PercentOfMonthlyPay() {
        // monthly = $5000, bonus = 15% = $750
        SalariedEmployee emp = EmployeeFactory.salaried("Carol", Money.of(60_000));

        assertThat(emp.calculateBonus().getAmount())
                .isEqualByComparingTo("750.00");
    }

    @Test
    void allEmployeeTypes_areAssignableFromEmployee() {
        List<Employee> employees = List.of(
                EmployeeFactory.commissioned("A", Money.of(500), 0.10, Money.of(1000)),
                EmployeeFactory.hourly("B", Money.of(15), 38),
                EmployeeFactory.salaried("C", Money.of(48_000))
        );

        employees.forEach(e -> {
            assertThat(e.calculatePay().getAmount()).isPositive();
            assertThat(e.calculateBonus().getAmount()).isPositive();
        });
    }
}
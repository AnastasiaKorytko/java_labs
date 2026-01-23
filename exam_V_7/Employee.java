package org.example;

import java.time.LocalDate;

public class Employee {
    private String name;
    private String department;
    private double salary;
    private LocalDate hireDate;

    public Employee(String name, String department, double salary, LocalDate hireDate) {
        this.name = name;
        this.department = department;
        this.salary = salary;
        this.hireDate = hireDate;
    }

    public String getName() { return name; }
    public String getDepartment() { return department; }
    public double getSalary() { return salary; }
    public LocalDate getHireDate() { return hireDate; }

    @Override
    public String toString() {
        return String.format("%s (%s, %.0f руб, нанят: %s)", name, department, salary, hireDate);
    }
}
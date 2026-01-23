package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        String fileName = "employees.txt";
        List<Employee> employees = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            employees = lines.map(line -> {
                String[] parts = line.split(",");
                return new Employee(parts[0], parts[1], Double.parseDouble(parts[2]), LocalDate.parse(parts[3]));
            }).collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
            return;
        }

        employees.stream().filter(e -> "IT".equals(e.getDepartment()))
                .max(Comparator.comparingDouble(Employee::getSalary))
                .ifPresent(e -> System.out.println("1. Сотрудник с максимальной зарплата в IT: " + e));

        double averageSalary = employees.stream().mapToDouble(Employee::getSalary).average().orElse(0);

        System.out.println("\n2. Сотрудники с зарплатой выше средней (" + (int)averageSalary + "):");
        System.out.printf("%-10s | %-10s\n", "Name", "Salary");
        employees.stream().filter(e -> e.getSalary() > averageSalary).forEach(e -> System.out.printf("%-10s | %-10.0f\n", e.getName(), e.getSalary()));

        System.out.println("\n3. Группировка по отделам:");
        Map<String, List<Employee>> byDept = employees.stream().collect(Collectors.groupingBy(Employee::getDepartment));

        System.out.printf("%-10s | %-30s\n", "Department", "Employees");
        byDept.forEach((dept, list) -> {
            String names = list.stream().map(Employee::getName).collect(Collectors.joining(", "));
            System.out.printf("%-10s | %-30s\n", dept, names);
        });

        Map<String, Double> totalSalaryByDept = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.summingDouble(Employee::getSalary)));
        System.out.println("\n4. Суммарная ЗП по отделам: " + totalSalaryByDept);

        List<Employee> Oldest = employees.stream()
                .sorted(Comparator.comparing(Employee::getHireDate))
                .limit(3)
                .collect(Collectors.toList());
        System.out.println("\n5. Топ-3 старых сотрудника: " + Oldest);

        Scanner scanner = new Scanner(System.in);
        System.out.print("\n6. Введите величину минимальной зарплаты: ");
        double inputMin = scanner.nextDouble();
        double doubleMin = inputMin * 2;
        boolean hasLowSalesSalary = employees.stream().anyMatch(e -> "Sales".equals(e.getDepartment()) && e.getSalary() < doubleMin);

        System.out.println("Есть ли в Sales зарплата < " + (int) doubleMin + ": " + (hasLowSalesSalary ? "Да" : "Нет"));
    }
}
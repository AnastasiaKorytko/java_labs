package org.example;
import java.util.Formatter;
import java.util.Scanner;

public class HyperbolicCos {
    public static double calculateSeries(double x, int k) {
        double sum = 1.0;
        double slag = 1.0;
        double e = Math.pow(10, -k);
        int n = 1;
        while (Math.abs(slag) > e) {
            slag = slag * x * x / ((2 * n - 1) * (2 * n));
            n++;
            sum += slag;
        }
        return sum;
    }
    public static void printRes(double x, int k, double sum, double mathCosh) {
        int p = k + 1;
        System.out.printf("sum %10." + p + "f%n", sum);
        System.out.printf("cosh %10." + p + "f%n", mathCosh);
        System.out.println();
        Formatter formatter = new Formatter();
        int roundSum = (int) Math.round(sum);
        int roundCosh = (int) Math.round(mathCosh);
        formatter.format("sum (8c/c): %#o%n", roundSum);
        formatter.format("sum (16c/c): %#x%n", roundSum);
        formatter.format("sum %+." + p + "f%n%n", sum);
        formatter.format("cosh (8c/c): %#o%n", roundCosh);
        formatter.format("cosh (16c/c): %#x%n", roundCosh);
        formatter.format("cosh %#." + p + "f%n", mathCosh);
        System.out.println(formatter.toString());
        formatter.close();
    }
}
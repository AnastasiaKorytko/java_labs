package org.example;

import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        double x = scan.nextDouble();
        int k = scan.nextInt();
        double myCosh = HyperbolicCos.calculateSeries(x, k);
        HyperbolicCos.printRes(x, k, myCosh, Math.cosh(x));
    }
}
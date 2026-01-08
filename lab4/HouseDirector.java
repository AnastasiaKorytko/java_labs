package org.example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

class HouseDirector {
    private HouseBuilder builder;

    public HouseDirector(HouseBuilder builder) {
        this.builder = builder;
    }

    public House constructStandardHouse(int id, String type, double area) {
        return builder.setId(id).setType(type).setArea(area).setRooms(3).setDate(new Date()).setPrice(area * 1000).build();
    }

    public House constructLuxuryHouse(int id, String type, double area) {
        return builder.setId(id).setType(type).setArea(area).setRooms(5).setDate(new Date()).setPrice(area * 2000).build();
    }

    public House constructEconomyHouse(int id, double area) {
        HouseBuilder economyBuilder = new EconomyHouseBuilder();
        return economyBuilder.setId(id).setArea(area).setRooms(2).setDate(new Date()).build();
    }

    public House constructPremiumHouse(int id, double area, int customRooms) {
        HouseBuilder premiumBuilder = new PremiumHouseBuilder();
        return premiumBuilder.setId(id).setArea(area).setRooms(customRooms).setDate(new Date()).build();
    }

    public House constructHouseFromConsole(Scanner scanner, int id) {
        try {
            System.out.print("Type: ");
            String type = scanner.nextLine();
            System.out.print("Area: ");
            double area = Double.parseDouble(scanner.nextLine());
            System.out.print("Number of rooms: ");
            int rooms = Integer.parseInt(scanner.nextLine());
            System.out.print("Construction date (dd.mm.yyyy): ");
            String dateStr = scanner.nextLine();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Date date = sdf.parse(dateStr);
            System.out.print("Price: ");
            double price = Double.parseDouble(scanner.nextLine());
            return builder.setId(id).setType(type).setArea(area).setRooms(rooms).setDate(date).setPrice(price).build();
        } catch (Exception e) {
            System.out.println("Error building house: " + e.getMessage());
            return null;
        }
    }
}
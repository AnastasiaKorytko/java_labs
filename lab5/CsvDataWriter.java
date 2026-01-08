package org.example;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

public class CsvDataWriter implements DataWriter {

    @Override
    public void write(List<House> houses, String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

            for (House house : houses) {
                pw.printf("%d,%s,%.1f,%d,%s,%.2f%n",
                        house.getId(),
                        house.getType(),
                        house.getArea(),
                        house.getRooms(),
                        sdf.format(house.getDate()),
                        house.getPrice());
            }

        } catch (Exception e) {
            System.out.println("Error writing CSV file: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "CSV Data Writer";
    }
}
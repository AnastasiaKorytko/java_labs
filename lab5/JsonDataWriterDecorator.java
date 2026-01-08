package org.example;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

public class JsonDataWriterDecorator extends DataWriterDecorator {

    public JsonDataWriterDecorator(DataWriter wrappee) {
        super(wrappee);
    }

    @Override
    public void write(List<House> houses, String filename) {
        if (!filename.toLowerCase().endsWith(".json")) {
            filename = filename + ".json";
        }
        createJsonDocument(houses, filename);

    }

    private void createJsonDocument(List<House> houses, String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

            pw.println("[");
            for (int i = 0; i < houses.size(); i++) {
                House house = houses.get(i);
                pw.println("  {");
                pw.println("    \"id\": " + house.getId() + ",");
                pw.println("    \"type\": \"" + escapeJson(house.getType()) + "\",");
                pw.println("    \"area\": " + house.getArea() + ",");
                pw.println("    \"rooms\": " + house.getRooms() + ",");
                pw.println("    \"date\": \"" + sdf.format(house.getDate()) + "\",");
                pw.println("    \"price\": " + house.getPrice());
                pw.print("  }");

                if (i < houses.size() - 1) {
                    pw.println(",");
                } else {
                    pw.println();
                }
            }
            pw.println("]");

        } catch (Exception e) {
            System.out.println("Error writing JSON file: " + e.getMessage());
        }
    }

    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + JSON Format";
    }
}
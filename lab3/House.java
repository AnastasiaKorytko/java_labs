package org.example;

import java.text.SimpleDateFormat;
import java.util.Date;

public class House {
    private int id;
    private String type;
    private double area;
    private int rooms;
    private Date date;
    private double price;

    public House(int id, String type, double area, int rooms, Date date, double price) {
        this.id = id;
        this.type = type;
        this.area = area;
        this.rooms = rooms;
        this.date = date;
        this.price = price;
    }

    public int getId() {
        return id;
    }
    public String getType() {
        return type;
    }
    public double getArea() {
        return area;
    }
    public int getRooms() {
        return rooms;
    }
    public Date getDate() {
        return date;
    }
    public double getPrice() {
        return price;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setArea(double area) {
        this.area = area;
    }
    public void setRooms(int rooms) {
        this.rooms = rooms;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return String.format("ID: %d  Type: %-10s  Area: %-6.1f  Rooms: %-2d  Date: %s  Price: %-10.1f", id, type, area, rooms, sdf.format(date), price);
    }
}

package org.example;

import java.util.Date;

interface HouseBuilder {
    HouseBuilder setId(int id);
    HouseBuilder setType(String type);
    HouseBuilder setArea(double area);
    HouseBuilder setRooms(int rooms);
    HouseBuilder setDate(Date date);
    HouseBuilder setPrice(double price);
    House build();
}

class ConcreteHouseBuilder implements HouseBuilder {
    private int id;
    private String type = "";
    private double area = 0.0;
    private int rooms = 0;
    private Date date = new Date();
    private double price = 0.0;

    @Override
    public HouseBuilder setId(int id) {
        this.id = id;
        return this;
    }

    @Override
    public HouseBuilder setType(String type) {
        this.type = type;
        return this;
    }

    @Override
    public HouseBuilder setArea(double area) {
        this.area = area;
        return this;
    }

    @Override
    public HouseBuilder setRooms(int rooms) {
        this.rooms = rooms;
        return this;
    }

    @Override
    public HouseBuilder setDate(Date date) {
        this.date = date;
        return this;
    }

    @Override
    public HouseBuilder setPrice(double price) {
        this.price = price;
        return this;
    }

    @Override
    public House build() {
        if (id <= 0) {
            throw new IllegalStateException("ID must be positive");
        }
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalStateException("Type cannot be empty");
        }
        if (area <= 0) {
            throw new IllegalStateException("Area must be positive");
        }
        if (rooms < 0) {
            throw new IllegalStateException("Rooms cannot be negative");
        }
        if (price < 0) {
            throw new IllegalStateException("Price cannot be negative");
        }
        return new House(id, type, area, rooms, date, price);
    }
}

class EconomyHouseBuilder implements HouseBuilder {
    private int id;
    private String type = "Economy";
    private double area = 0.0;
    private int rooms = 0;
    private Date date = new Date();
    private double price = 0.0;

    @Override
    public HouseBuilder setId(int id) {
        this.id = id;
        return this;
    }

    @Override
    public HouseBuilder setType(String type) {
        this.type = "Economy";
        return this;
    }

    @Override
    public HouseBuilder setArea(double area) {
        if (area > 100) {
            this.area = 100;
        } else {
            this.area = area;
        }
        return this;
    }

    @Override
    public HouseBuilder setRooms(int rooms) {
        if (rooms > 3) {
            this.rooms = 3;
        } else {
            this.rooms = rooms;
        }
        return this;
    }

    @Override
    public HouseBuilder setDate(Date date) {
        this.date = date;
        return this;
    }

    @Override
    public HouseBuilder setPrice(double price) {
        this.price = area * 800;
        return this;
    }

    @Override
    public House build() {
        if (id <= 0) throw new IllegalStateException("ID must be positive");
        if (area <= 0) throw new IllegalStateException("Area must be positive");
        return new House(id, type, area, rooms, date, price);
    }
}

class PremiumHouseBuilder implements HouseBuilder {
    private int id;
    private String type = "Premium";
    private double area = 0.0;
    private int rooms = 0;
    private Date date = new Date();
    private double price = 0.0;

    @Override
    public HouseBuilder setId(int id) {
        this.id = id;
        return this;
    }

    @Override
    public HouseBuilder setType(String type) {
        this.type = "Premium";
        return this;
    }

    @Override
    public HouseBuilder setArea(double area) {
        if (area < 150) {
            this.area = 150;
        } else {
            this.area = area;
        }
        return this;
    }

    @Override
    public HouseBuilder setRooms(int rooms) {
        if (rooms < 4) {
            this.rooms = 4;
        } else {
            this.rooms = rooms;
        }
        return this;
    }

    @Override
    public HouseBuilder setDate(Date date) {
        this.date = date;
        return this;
    }

    @Override
    public HouseBuilder setPrice(double price) {
        this.price = area * 2000;
        return this;
    }

    @Override
    public House build() {
        if (id <= 0) throw new IllegalStateException("ID must be positive");
        if (area < 150) throw new IllegalStateException("Premium house must have at least 150 sq.m.");
        return new House(id, type, area, rooms, date, price);
    }
}
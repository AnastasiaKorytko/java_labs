package org.example;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HouseListStorage extends DataStorage {
    private List<House> housesList;

    public HouseListStorage() {
        housesList = new ArrayList<>();
    }

    @Override
    public void addHouse(House house) {
        for (House existingHouse : housesList) {
            if (existingHouse.getId() == house.getId()) {
                System.out.println("House with ID " + house.getId() + " already exists");
                return;
            }
        }
        housesList.add(house);
    }

    @Override
    public void updateHouse(int id, House updatedHouse) {
        for (int i = 0; i < housesList.size(); i++) {
            if (housesList.get(i).getId() == id) {
                housesList.set(i, updatedHouse);
                return;
            }
        }
        System.out.println("House not found");
    }

    @Override
    public void deleteHouse(int id) {
        boolean removed = housesList.removeIf(house -> house.getId() == id);
        if (!removed) {
            System.out.println("House not found");
        }
    }

    @Override
    public void displayAll() {
        System.out.println();
        System.out.println("LIST STORAGE");
        Iterator<House> iterator = housesList.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    @Override
    public House getHouseById(int id) {
        for (House house : housesList) {
            if (house.getId() == id) {
                return house;
            }
        }
        return null;
    }

    @Override
    public List<House> getAllHouses() {
        return housesList;
    }

}

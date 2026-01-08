package org.example;

import java.util.*;

public class HouseMapStorage extends DataStorage {
    private Map<Integer, House> housesMap;

    public HouseMapStorage() {
        housesMap = new LinkedHashMap<>();
    }

    @Override
    public void addHouse(House house) {
        if (housesMap.containsKey(house.getId())) {
            System.out.println("House with ID " + house.getId() + " already exists");
            return;
        }
        housesMap.put(house.getId(), house);
    }

    @Override
    public void updateHouse(int id, House updatedHouse) {
        if (housesMap.containsKey(id)) {
            housesMap.put(id, updatedHouse);
        } else {
            System.out.println("House not found");
        }
    }

    @Override
    public void deleteHouse(int id) {
        House removedHouse = housesMap.remove(id);
        if (removedHouse == null) {
            System.out.println("House not found");
        }
    }

    @Override
    public void displayAll() {
        System.out.println();
        System.out.println("MAP STORAGE");
        Iterator<Map.Entry<Integer, House>> iterator = housesMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, House> entry = iterator.next();
            System.out.println(entry.getValue());
        }
    }

    @Override
    public House getHouseById(int id) {
        return housesMap.get(id);
    }

    @Override
    public List<House> getAllHouses() {
        return new ArrayList<>(housesMap.values());
    }

    public Map<Integer, House> getHousesMap() {
        return housesMap;
    }
}

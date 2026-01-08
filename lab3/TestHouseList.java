import org.example.House;
import org.example.HouseListStorage;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestHouseList {

    private HouseListStorage storage;
    private House house;

    @BeforeEach
    void setUp() {
        storage = new HouseListStorage();
        house = new House(1, "Flat", 45.0, 2, new Date(), 50000);
    }

    @Test
    void testAddHouse() {
        storage.addHouse(house);
        assertEquals(1, storage.getAllHouses().size());
        assertEquals(house, storage.getHouseById(1));
    }

    @Test
    void testAddDuplicateId() {
        storage.addHouse(house);
        storage.addHouse(house);
        assertEquals(1, storage.getAllHouses().size());
    }

    @Test
    void testDeleteHouse() {
        storage.addHouse(house);
        storage.deleteHouse(1);
        assertNull(storage.getHouseById(1));
    }

    @Test
    void testUpdateHouse() {
        storage.addHouse(house);
        House updated = new House(1, "Villa", 100, 4, new Date(), 150000);
        storage.updateHouse(1, updated);
        assertEquals("Villa", storage.getHouseById(1).getType());
        assertEquals(100, storage.getHouseById(1).getArea());
    }
}


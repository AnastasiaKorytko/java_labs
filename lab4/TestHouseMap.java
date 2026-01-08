import org.example.House;
import org.example.HouseMapStorage;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestHouseMap {

    private HouseMapStorage storage;
    private House house;

    @BeforeEach
    void setUp() {
        storage = new HouseMapStorage();
        house = new House(1, "Flat", 45.0, 2, new Date(), 50000);
    }

    @Test
    void testAddHouse() {
        storage.addHouse(house);
        assertEquals(house, storage.getHouseById(1));
    }

    @Test
    void testUpdateHouse() {
        storage.addHouse(house);
        House updated = new House(1, "Cottage", 120, 5, new Date(), 200000);
        storage.updateHouse(1, updated);
        assertEquals("Cottage", storage.getHouseById(1).getType());
    }

    @Test
    void testDeleteHouse() {
        storage.addHouse(house);
        storage.deleteHouse(1);
        assertNull(storage.getHouseById(1));
    }
}

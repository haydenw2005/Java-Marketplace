import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * Class to test Item Class.
 *
 * <p>
 * Purdue University -- CS18000 -- Fall 2023
 * </p>
 *
 * @author Soham
 * @version November 13, 2023
 */

public class ItemTest {

    private Item item;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        item = new Item("Laptop", "High-performance laptop", 5, 1, 1200.0,
                new HashMap<>(), new HashMap<>());
        objectMapper = new ObjectMapper();
    }

    @Test
    public void gettersAndSettersTest() {
        assertEquals("Laptop", item.getName());
        assertEquals("High-performance laptop", item.getDescription());
        assertEquals(1, item.getCount());
        assertEquals(1200.0, item.getPrice(), 0.001);
        assertEquals(5, item.getStock());

        item.setCount(2);
        item.setStock(10);
        item.setPrice(1500.0);
        item.setName("Desktop");
        item.setDescription("Powerful desktop computer");

        assertEquals(2, item.getCount());
        assertEquals(10, item.getStock());
        assertEquals(1500.0, item.getPrice(), 0.001);
        assertEquals("Desktop", item.getName());
        assertEquals("Powerful desktop computer", item.getDescription());
    }

    @Test
    public void totalBoughtByBuyerTest() {
        item.setTotalBoughtByBuyer("testUser", 3);

        assertEquals(3, item.totalBoughtByBuyer("testUser"));
    }

    @Test
    public void toStringStockTest() {
        String expected = "Product: Laptop | Price: 1200.0 | Stock: 5";

        assertEquals(expected, item.toString());
    }

    @Test
    public void toStringCountTest() {
        item.setStock(-1); // Simulate item with count

        String expected = "Product: Laptop | Price: 1200.0 | Count: 1";

        assertEquals(expected, item.toString());
    }

    @Test
    public void toStringInvalidInputTest() {
        item.setStock(-1);
        item.setCount(-1); // Simulate invalid input

        String result = item.toString();

        assertEquals(null, result);
    }
}

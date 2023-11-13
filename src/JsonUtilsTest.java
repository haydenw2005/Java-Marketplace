import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Class to test JsonUtils Class.
 *
 * <p>
 * Purdue University -- CS18000 -- Fall 2023
 * </p>
 *
 * @author Soham
 * @version November 13, 2023
 */

public class JsonUtilsTest {
    private Item item;
    private Seller seller;

    @Before
    public void setUp() {
        String username = "john_doe";
        String password = "password123";
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";

        Map<String, Item> stockItems = new HashMap<>();
        item = new Item("item1", "Description", 10, 5, 20.0, null, null);
        stockItems.put("item1", item);

        Map<String, Store> stores = new HashMap<>();
        Store store = new Store("store1", stockItems, new HashMap<>());
        stores.put("store1", store);

        seller = new Seller(username, password, firstName, lastName, email, stores);
    }

    @Test
    public void addObjectToJsonValidInput() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String nodeDir = "/sellers";
        String newObjectKey = seller.getUsername();

        JsonUtils.addObjectToJson(nodeDir, newObjectKey, seller, objectMapper);

        assertTrue(JsonUtils.hasKey(nodeDir, newObjectKey, objectMapper));

        // Clean up
        JsonUtils.removeObjectFromJson(nodeDir, newObjectKey, objectMapper);
    }

    @Test
    public void removeObjectFromJsonValidInput() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        String nodeDir = "/sellers";
        String objectKey = seller.getUsername();
        JsonUtils.addObjectToJson(nodeDir, objectKey, seller, objectMapper);

        assertTrue(JsonUtils.hasKey(nodeDir, objectKey, objectMapper));

        // Remove the item
        JsonUtils.removeObjectFromJson(nodeDir, objectKey, objectMapper);

        assertFalse(JsonUtils.hasKey(nodeDir, objectKey, objectMapper));
    }

    @Test
    public void objectByKey() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String nodeDir = "/sellers";
        String objectKey = seller.getUsername();
        Seller expectedSeller = seller;

        JsonUtils.addObjectToJson(nodeDir, objectKey, expectedSeller, objectMapper);

        Seller retreivedSeller = JsonUtils.objectByKey(objectMapper, nodeDir + "/" + objectKey, Seller.class);

        assertEquals(expectedSeller.getFirstName(), retreivedSeller.getFirstName());
        assertEquals(expectedSeller.getLastName(), retreivedSeller.getLastName());
        assertEquals(expectedSeller.getUsername(), retreivedSeller.getUsername(), "john_doe");
        assertEquals(expectedSeller.getEmail(), retreivedSeller.getEmail());

        // Clean up
        JsonUtils.removeObjectFromJson(nodeDir, objectKey, objectMapper);
    }

    @Test
    public void hasKeyValidInput() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String nodeDir = "/sellers";
        String objectKey = seller.getUsername();

        JsonUtils.addObjectToJson(nodeDir, objectKey, item, objectMapper);

        assertTrue(JsonUtils.hasKey(nodeDir, objectKey, objectMapper));

        // Clean up
        JsonUtils.removeObjectFromJson(nodeDir, objectKey, objectMapper);
    }

    @Test
    public void readJsonFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String nodeDir = "/sellers";
        String objectKey = seller.getUsername();

        JsonUtils.addObjectToJson(nodeDir, objectKey, item, objectMapper);

        JsonNode rootNode = JsonUtils.readJsonFile(objectMapper);

        assertNotNull(rootNode);

        // Clean up
        JsonUtils.removeObjectFromJson(nodeDir, objectKey, objectMapper);
    }

    @Test
    public void serializeToJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String nodeDir = "/sellers";
        String objectKey = seller.getUsername();

        JsonUtils.addObjectToJson(nodeDir, objectKey, item, objectMapper);

        JsonNode rootNode = JsonUtils.readJsonFile(objectMapper);
        String jsonString = JsonUtils.serializeToJson(rootNode);

        assertNotNull(jsonString);
        assertTrue(jsonString.contains(objectKey));

        // Clean up
        JsonUtils.removeObjectFromJson(nodeDir, objectKey, objectMapper);
    }
}

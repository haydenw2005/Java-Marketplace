import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
/**
 * Class to test Seller Class.
 *
 * <p>
 * Purdue University -- CS18000 -- Fall 2023
 * </p>
 *
 * @author Soham
 * @version November 13, 2023
 */

public class SellerTest {

    @Test
    public void testCreateNewStore() {
        Seller seller = new Seller("test_seller", "password", "John", "Doe", "john@example.com", new HashMap<>());
        ObjectMapper objectMapper = new ObjectMapper();

        seller.createNewStore("TestStore", objectMapper);

        assertTrue(seller.getStores().containsKey("TestStore"));
        assertEquals("TestStore", seller.getStores().get("TestStore").getName());
    }

    @Test
    public void testEditStore() throws IOException {
        Map<String, Store> stores = new HashMap<>();
        stores.put("TestStore", new Store("TestStore", new HashMap<>(), new HashMap<>()));
        Seller seller = new Seller("test_seller", "password", "John", "Doe", "john@example.com", stores);
        ObjectMapper objectMapper = new ObjectMapper();
        seller.createNewStore(stores.get("TestStore").getName(), objectMapper);

        seller.editStore("TestStore", "EditedStore", objectMapper);
        String dir = "/sellers/" + seller.getUsername() + "/stores";
        assertFalse(JsonUtils.hasKey(dir, "TestStore", objectMapper));
        assertTrue(JsonUtils.hasKey(dir, "EditedStore", objectMapper));
        // Store name was edited
    }

    @Test
    public void testGetAllStoreItemsStock() {
        // Arrange
        Map<String, Item> stockItems = new HashMap<>();
        stockItems.put("StockItem1", new Item("StockItem1", "Description", 10, 5, 20.0, null, new HashMap<>()));
        stockItems.put("StockItem2", new Item("StockItem2", "Description", 8, 3, 15.0, null, new HashMap<>()));
        Map<String, Store> stores = new HashMap<>();
        stores.put("TestStore", new Store("TestStore", stockItems, new HashMap<>()));
        Seller seller = new Seller("test_seller", "password", "John", "Doe", "john@example.com", stores);

        // Act
        ArrayList<Item> allStoreItems = seller.getAllStoreItems("stock");

        // Assert
        assertEquals(2, allStoreItems.size());
        assertTrue(allStoreItems.stream().anyMatch(item -> item.getName().equals("StockItem1")));
        assertTrue(allStoreItems.stream().anyMatch(item -> item.getName().equals("StockItem2")));
    }

    @Test
    public void testGetAllStoreItemsSold() {
        // Arrange
        Map<String, Item> soldItems = new HashMap<>();
        soldItems.put("SoldItem1", new Item("SoldItem1", "Description", 5, 2, 18.0, null, new HashMap<>()));
        soldItems.put("SoldItem2", new Item("SoldItem2", "Description", 8, 4, 25.0, null, new HashMap<>()));
        Map<String, Store> stores = new HashMap<>();
        stores.put("TestStore", new Store("TestStore", new HashMap<>(), soldItems));
        Seller seller = new Seller("test_seller", "password", "John", "Doe", "john@example.com", stores);

        // Act
        ArrayList<Item> allStoreItems = seller.getAllStoreItems("sold");

        // Assert
        assertEquals(2, allStoreItems.size());
        assertTrue(allStoreItems.stream().anyMatch(item -> item.getName().equals("SoldItem1")));
        assertTrue(allStoreItems.stream().anyMatch(item -> item.getName().equals("SoldItem2")));
    }

    @Test
    public void testGetAllStoreItemsInvalidType() {
        Map<String, Store> stores = new HashMap<>();
        stores.put("TestStore", new Store("TestStore", new HashMap<>(), new HashMap<>()));
        Seller seller = new Seller("test_seller", "password", "John", "Doe", "john@example.com", stores);

        ArrayList<Item> allStoreItems = seller.getAllStoreItems("invalid");

        assertNull(allStoreItems);
    }

    @Test
    public void testGetStoreByItem() {
        Map<String, Item> stockItemsStore1 = new HashMap<>();
        stockItemsStore1.put("StockItem1", new Item("StockItem1", "Description", 10, 5, 20.0, null, new HashMap<>()));
        stockItemsStore1.put("StockItem2", new Item("StockItem2", "Description", 8, 3, 15.0, null, new HashMap<>()));
        Map<String, Item> stockItemsStore2 = new HashMap<>();
        stockItemsStore2.put("StockItem3", new Item("StockItem3", "Description", 12, 6, 25.0, null, new HashMap<>()));
        stockItemsStore2.put("StockItem4", new Item("StockItem4", "Description", 15, 8, 30.0, null, new HashMap<>()));
        Map<String, Store> stores = new HashMap<>();
        stores.put("Store1", new Store("Store1", stockItemsStore1, new HashMap<>()));
        stores.put("Store2", new Store("Store2", stockItemsStore2, new HashMap<>()));
        Seller seller = new Seller("test_seller", "password", "John", "Doe", "john@example.com", stores);
        Item searchItem = new Item("StockItem2", "Description", 0, 0, 0.0, null, new HashMap<>());

        Store foundStore = seller.getStoreByItem(searchItem);

        assertNotNull(foundStore);
        assertEquals("Store1", foundStore.getName());
    }
}

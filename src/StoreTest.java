import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * Class to test Store Class.
 *
 * <p>
 * Purdue University -- CS18000 -- Fall 2023
 * </p>
 *
 * @author Soham
 * @version November 13, 2023
 */

public class StoreTest {
    private Buyer buyer;
    private Seller seller;
    private ObjectMapper objectMapper;
    private Marketplace marketplace;

    @Before
    public void setUp() {
        buyer = new Buyer("testingbuyer", "test", "john", "doe", "john.doe@example.com",
                new HashMap<>(), new HashMap<>());
        seller = new Seller("testseller", "test", "john", "doe", "john.doe@example.com",
                new HashMap<>());
        objectMapper = new ObjectMapper();
        marketplace = new Marketplace(new HashMap<String, Buyer>() {
            {
                put(buyer.getUsername(), buyer);
            }
        },
                new HashMap<String, Seller>() {
                    {
                        put(seller.getUsername(), seller);
                    }
                });
    }

    @Test
    public void testAddToStockItems() throws IOException {
        Store store = new Store("TestStore", new HashMap<>(), new HashMap<>());
        Item item = new Item("TestItem", "Description", 10, 5, 20.0, null, new HashMap<>());

        seller.createNewStore(store.getName(), objectMapper);

        String dir = "/sellers/" + seller.getUsername() + "/stores/" + store.getName() + "/stockItems";

        store.addToStockItems(item, "testseller", objectMapper);

        assertTrue(JsonUtils.hasKey(dir, "TestItem", objectMapper)); // check if json has store directory and that has
                                                                     // an item key
    }

    @Test
    public void testDeleteItem() throws IOException {
        Map<String, Item> stockItems = new HashMap<>();
        Item item2 = new Item("TestItem2", "Description", 10, 5, 20.0, null, new HashMap<>());
        Store store = new Store("TestStore2", stockItems, new HashMap<>());
        seller.createNewStore(store.getName(), objectMapper);
        store.addToStockItems(item2, seller.getUsername(), objectMapper);
        store.deleteItem(seller.getUsername(), "TestItem2", objectMapper);

        String dir = "/sellers/" + seller.getUsername() + "/stores/" + store.getName() + "/stockItems";

        assertFalse(JsonUtils.hasKey(dir, "TestItem2", objectMapper));
    }

    @Test
    public void testNumProductsSold() {
        Map<String, Item> soldItems = new HashMap<>();
        soldItems.put("SoldItem1", new Item("SoldItem1", "Description", 5, 0, 15.0, null, new HashMap<>()));
        soldItems.put("SoldItem2", new Item("SoldItem2", "Description", 8, 0, 25.0, null, new HashMap<>()));
        Store store = new Store("TestStore", new HashMap<>(), soldItems);

        int numSold = store.numProductsSold();

        assertEquals(13, numSold);
    }

    @Test
    public void testGetProductsPurchasedFromStore() {
        Item purchasedItem = new Item("PurchasedItem", "Description", 5, 0, 15.0, null, new HashMap<>());
        buyer.addToPurchaseHistory(purchasedItem, new ObjectMapper());
        Store store = new Store("TestStore",
                new HashMap<>() {
                    {
                        put(purchasedItem.getName(), purchasedItem);
                    }
                }, new HashMap<>());
        seller.setStores(new HashMap<>() {
            {
                put(store.getName(), store);
            }
        });

        ArrayList<Item> productsPurchased = store.getProductsPurchasedFromStore(marketplace, buyer);

        assertTrue(productsPurchased.contains(purchasedItem));
    }

    @Test
    public void testGetTotalItemsPurchased() {
        Item purchasedItem1 = new Item("PurchasedItem1", "Description", 5, 5, 15.0, null, new HashMap<>());
        Item purchasedItem2 = new Item("PurchasedItem2", "Description", 3, 3, 12.0, null, new HashMap<>());
        buyer.addToPurchaseHistory(purchasedItem1, new ObjectMapper());
        buyer.addToPurchaseHistory(purchasedItem2, new ObjectMapper());
        Store store = new Store("TestStore",
                new HashMap<>() {
                    {
                        put(purchasedItem1.getName(), purchasedItem1);
                        put(purchasedItem2.getName(), purchasedItem2);
                    }
                }, new HashMap<>());
        seller.setStores(new HashMap<>() {
            {
                put(store.getName(), store);
            }
        });

        int totalItemsPurchased = store.getTotalItemsPurchased(marketplace, buyer);

        assertEquals(8, totalItemsPurchased);
    }
}

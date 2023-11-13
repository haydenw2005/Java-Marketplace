import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BuyerTest {
    private Buyer buyer;
    private Marketplace marketplace;
    private ObjectMapper objectMapper;
    private ByteArrayOutputStream outContent;

    @Before
    public void setUp() {
        buyer = new Buyer("testingbuyer", "test", "john", "doe", "john.doe@example.com",
                new HashMap<>(), new HashMap<>());
        marketplace = new Marketplace();
        objectMapper = new ObjectMapper();
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testAddItemToCart() {
        Item item = new Item("Laptop", "A laptop.", 5, 2, 1000,
                null, new HashMap<String, Integer>() {
                    {
                        put("seller", 5);
                    }
                });
        buyer.addItemToCart(item, objectMapper);

        assertTrue(buyer.getCart().containsKey("Laptop"));
        assertEquals(1, buyer.getCart().size());
        assertEquals(2, buyer.getCart().get("Laptop").getCount()); // check if count is 2
    }

    @Test
    public void testAddItemToCart_NotEnoughStock() {
        Item item = new Item("Bread", "Delicious bread.", 2, 10, 1000,
                null, new HashMap<String, Integer>() {
                    {
                        put("seller2", 2);
                    }
                });
        buyer.addItemToCart(item, objectMapper);

        assertFalse(buyer.getCart().containsKey("Phone"));
        assertEquals(0, buyer.getCart().size());
    }

    @Test
    public void testAddToPurchaseHistory() {
        Item item = new Item("Keyboard", "Mechanical keyboard.", 3, 2, 80.0,
                new HashMap<>(), new HashMap<>());

        buyer.addToPurchaseHistory(item, objectMapper);

        assertTrue(buyer.getPurchaseHistory().containsKey("Keyboard"));
        assertEquals(1, buyer.getPurchaseHistory().size());
        assertEquals(-1, item.getStock()); // Verify stock is set to -1
    }

    @Test
    public void testBuyCart() {
        Item item1 = new Item("Headphones", "Noise-canceling headphones", 2, 1, 150.0,
                new HashMap<>(), new HashMap<String, Integer>() {
                    {
                        put("seller", 2);
                    }
                });
        Item item2 = new Item("Monitor", "High-resolution computer monitor", 1, 1, 300.0,
                new HashMap<>(), new HashMap<String, Integer>() {
                    {
                        put("seller", 1);
                    }
                });

        buyer.addItemToCart(item1, objectMapper);
        buyer.addItemToCart(item2, objectMapper);

        buyer.buyCart(marketplace, objectMapper);
        buyer.addToPurchaseHistory(item1, objectMapper);
        buyer.addToPurchaseHistory(item2, objectMapper);

        assertTrue(buyer.getCart().isEmpty());
        assertEquals(2, buyer.getPurchaseHistory().size());
        assertEquals(item1, buyer.getPurchaseHistory().get("Headphones"));
        assertEquals(item2, buyer.getPurchaseHistory().get("Monitor"));

    }

    @Test
    public void testBuyItem() {
        Item item = new Item("Tablet", "Portable tablet device", 3, 2, 300.0,
                new HashMap<>(), new HashMap<String, Integer>() {
                    {
                        put("seller", 3);
                    }
                });

        buyer.buyItem(item, marketplace, objectMapper);
        assertFalse(buyer.getCart().containsKey("Tablet"));

        buyer.updateStock(item);
        assertEquals(1, item.getStock()); // Stock reduced after purchase (3 - 2)

        buyer.addToPurchaseHistory(item, objectMapper);
        assertEquals(1, buyer.getPurchaseHistory().size());

    }

    @Test
    public void showAllCartItems_NotEmpty() {
        Item item1 = new Item("Laptop", "High-performance laptop", 5, 1, 1200.0,
                new HashMap<>(), new HashMap<String, Integer>() {
                    {
                        put("seller", 5);
                    }
                });
        Item item2 = new Item("Headphones", "Noise-canceling headphones", 2, 2, 150.0,
                new HashMap<>(), new HashMap<String, Integer>() {
                    {
                        put("seller", 2);
                    }
                });

        buyer.addItemToCart(item1, objectMapper);
        buyer.addItemToCart(item2, objectMapper);

        buyer.showAllCartItems();

        String expectedOutput = "Product: Headphones | Price: 150.0 | Stock: 2\n" +
                "Product: Laptop | Price: 1200.0 | Stock: 5\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void showAllCartItems_Empty() {
        buyer.showAllCartItems();

        assertEquals("Cart is empty! Add items to cart from the Marketplace.\n", outContent.toString());
    }

    @Test
    public void showPurchaseHistory_NotEmpty() {
        Item item1 = new Item("Laptop", "High-performance laptop", 5, 1, 1200.0,
                new HashMap<>(), new HashMap<>());
        Item item2 = new Item("Headphones", "Noise-canceling headphones", 2, 2, 150.0,
                new HashMap<>(), new HashMap<>());
        buyer.setPurchaseHistory(new HashMap<>());

        buyer.addToPurchaseHistory(item1, objectMapper);
        buyer.addToPurchaseHistory(item2, objectMapper);

        buyer.showPurchaseHistory();

        String expectedOutput = "\nPurchase history:\n\n" +
                "Product: Headphones | Price: 150.0 | Count: 2\n" +
                "Product: Laptop | Price: 1200.0 | Count: 1\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void showPurchaseHistory_Empty() {
        Buyer buyerNoHist = new Buyer("testingbuyer2", "test", "john", "doe", "john.doe@example.com",
                new HashMap<>(), new HashMap<>());
        buyerNoHist.showPurchaseHistory();

        assertEquals("No items purchased yet.\n", outContent.toString());
    }
}

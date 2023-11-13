import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class to test Buyer Class.
 *
 * <p>
 * Purdue University -- CS18000 -- Fall 2023
 * </p>
 *
 * @author Soham
 * @version November 13, 2023
 */

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
    public void testAddItemToCartNotEnoughStock() {
        Item item = new Item("Bread", "Delicious bread.", 2, 10, 1000,
                null, new HashMap<String, Integer>() {
                    {
                        put("seller2", 2);
                    }
                });
        buyer.addItemToCart(item, objectMapper);

        assertFalse(buyer.getCart().containsKey("Bread"));
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
    public void showAllCartItemsNotEmpty() {
        Item item1 = new Item("toaster", "High-performance toaster", 5, 1, 1200.0,
                new HashMap<>(), new HashMap<String, Integer>() {
                    {
                        put("seller", 5);
                    }
                });
        Item item2 = new Item("earphones", "Noise-canceling earphones", 2, 2, 150.0,
                new HashMap<>(), new HashMap<String, Integer>() {
                    {
                        put("seller", 2);
                    }
                });

        buyer.addItemToCart(item1, objectMapper);
        buyer.addItemToCart(item2, objectMapper);

        buyer.showAllCartItems();

        String expectedOutput = "Product: toaster | Price: 1200.0 | Stock: 5\n"
                + "Product: earphones | Price: 150.0 | Stock: 2\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void showAllCartItemsEmpty() {
        buyer.showAllCartItems();

        assertEquals("Cart is empty! Add items to cart from the Marketplace.\n", outContent.toString());
    }

    @Test
    public void showPurchaseHistory_NotEmpty() {
        Item item1 = new Item("toaster", "High-performance toaster", 5, 1, 1200.0,
                new HashMap<>(), new HashMap<>());
        Item item2 = new Item("earphones", "Noise-canceling earphones", 2, 2, 150.0,
                new HashMap<>(), new HashMap<>());
        buyer.setPurchaseHistory(new HashMap<>());

        buyer.addToPurchaseHistory(item1, objectMapper);
        buyer.addToPurchaseHistory(item2, objectMapper);

        buyer.showPurchaseHistory();

        String expectedOutput = "\nPurchase history:\n" +
                "Product: toaster | Price: 1200.0 | Count: 1\n" +
                "Product: earphones | Price: 150.0 | Count: 2\n";
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
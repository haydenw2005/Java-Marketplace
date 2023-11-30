/*
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Class to test CsvUtilsClass.
 *
 * <p>
 * Purdue University -- CS18000 -- Fall 2023
 * </p>
 *
 * @author Soham
 * @version November 13, 2023
 *
public class CsvUtilsTest {

    private Buyer buyer;
    private Seller seller;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        buyer = new Buyer("testingbuyer", "test", "john", "doe", "john.doe@example.com",
                new HashMap<>(), new HashMap<>());
        seller = new Seller("testseller", "test", "john", "doe", "john.doe@example.com",
                new HashMap<>());
        objectMapper = new ObjectMapper();
    }

    @Test
    public void writePurchaseHistoryToCSVSuccess() throws IOException {
        Item item1 = new Item("toaster", "High-performance toaster", 5, 1, 1200.0,
                new HashMap<>(), new HashMap<>());
        Item item2 = new Item("earphones", "Noise-canceling earphones", 2, 2, 150.0,
                new HashMap<>(), new HashMap<>());

        buyer.addToPurchaseHistory(item1, objectMapper);
        buyer.addToPurchaseHistory(item2, objectMapper);

        CsvUtils.writePurchaseHistoryToCSV("purchaseHistory", buyer);

        File file = new File("purchaseHistory.csv");
        assertTrue(file.exists());

        // Cleanup
        file.delete();
    }

    @Test
    public void writeProductsToCSVSuccess() throws IOException {
        Store store = new Store("Electronics", new HashMap<>(), new HashMap<>());
        Item item1 = new Item("toaster", "High-performance toaster", 5, 1, 1200.0,
                new HashMap<>(), new HashMap<>());
        Item item2 = new Item("earphones", "Noise-canceling earphones", 2, 2, 150.0,
                new HashMap<>(), new HashMap<>());
        store.addToStockItems(item1, seller.getUsername(), objectMapper);
        store.addToStockItems(item2, seller.getUsername(), objectMapper);
        seller.createNewStore(store.getName(), objectMapper);

        CsvUtils.writeProductsToCSV("sellerProducts", seller);

        File file = new File("sellerProducts.csv");
        assertTrue(file.exists());

        file.delete(); // cleanup
    }

    @Test
    public void importFromCSVSuccess() throws IOException {
        // create CSV file for test
        File testFile = new File("importTest.csv");
        testFile.createNewFile();
        String[] testData = { "Product,Description,Price,Stock,Store",
            "toaster,High-performance toaster,1200.0,5,Electronics" };
        CsvUtilsTestUtils.writeToFile(testFile, testData);

        CsvUtils.importFromCSV("importTest", seller, objectMapper);

        assertTrue(seller.getStores().containsKey("Electronics"));
        assertEquals(1, seller.getStores().get("Electronics").getStockItems().size());

        testFile.delete(); // cleanup
    }

    @Test
    public void writePurchaseHistoryToCSVInvalidFilename() {
        Buyer newBuyer = new Buyer("testUser", "password", "John", "Doe", "john.doe@example.com",
                new HashMap<>(), new HashMap<>());

        CsvUtils.writePurchaseHistoryToCSV("", newBuyer); // Empty filename

        File file = new File("");
        assertFalse(file.exists());
    }

    @Test
    public void writeProductsToCSVInvalidFilename() {
        Seller newSeller = new Seller("sellerUser", "password", "Seller", "One", "seller.one@example.com",
                new HashMap<>());

        CsvUtils.writeProductsToCSV("", newSeller); // Empty filename

        File file = new File("");
        assertFalse(file.exists());
    }

    @Test
    public void importFromCSVInvalidFilename() {
        Seller newSeller = new Seller("sellerUser", "password", "Seller", "One", "seller.one@example.com",
                new HashMap<>());
        ObjectMapper newObjectMapper = new ObjectMapper();

        try {
            CsvUtils.importFromCSV("", newSeller, newObjectMapper); // Empty filename

            // No changes should be made since the filename is invalid
            assertFalse(newSeller.getStores().containsKey("Electronics"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void importFromCSVNonExistingFile() {
        Seller newSeller = new Seller("sellerUser", "password", "Seller", "One", "seller.one@example.com",
                new HashMap<>());
        ObjectMapper newObjectMapper = new ObjectMapper();

        try {
            CsvUtils.importFromCSV("nonExistingFile", newSeller, newObjectMapper);

            // No changes should be made since the file does not exist
            assertFalse(newSeller.getStores().containsKey("Electronics"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/**
 * Additional utils class.
 *
 * <p>
 * Purdue University -- CS18000 -- Fall 2023
 * </p>
 *
 * @author Soham
 * @version November 13, 2023
 *
class CsvUtilsTestUtils {
    public static void writeToFile(File file, String[] data) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(file, false))) {
            for (int i = 0; i < data.length; i++) {
                writer.println(data[i]);
            }
        }
    }
}
 */
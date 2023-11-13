import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
    public void writePurchaseHistoryToCSV_Success() throws IOException {
        Item item1 = new Item("Laptop", "High-performance laptop", 5, 1, 1200.0,
                new HashMap<>(), new HashMap<>());
        Item item2 = new Item("Headphones", "Noise-canceling headphones", 2, 2, 150.0,
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
    public void writeProductsToCSV_Success() throws IOException {
        Store store = new Store("Electronics", new HashMap<>(), new HashMap<>());
        Item item1 = new Item("Laptop", "High-performance laptop", 5, 1, 1200.0,
                new HashMap<>(), new HashMap<>());
        Item item2 = new Item("Headphones", "Noise-canceling headphones", 2, 2, 150.0,
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
    public void importFromCSV_Success() throws IOException {
        // create CSV file for test
        File testFile = new File("importTest.csv");
        testFile.createNewFile();
        String[] testData = { "Product,Description,Price,Stock,Store",
                "Laptop,High-performance laptop,1200.0,5,Electronics" };
        CsvUtilsTestUtils.writeToFile(testFile, testData);

        CsvUtils.importFromCSV("importTest", seller, objectMapper);

        assertTrue(seller.getStores().containsKey("Electronics"));
        assertEquals(1, seller.getStores().get("Electronics").getStockItems().size());

        testFile.delete(); // cleanup
    }

    @Test
    public void writePurchaseHistoryToCSV_InvalidFilename() {
        Buyer buyer = new Buyer("testUser", "password", "John", "Doe", "john.doe@example.com",
                new HashMap<>(), new HashMap<>());

        CsvUtils.writePurchaseHistoryToCSV("", buyer); // Empty filename

        File file = new File("");
        assertFalse(file.exists());
    }

    @Test
    public void writeProductsToCSV_InvalidFilename() {
        Seller seller = new Seller("sellerUser", "password", "Seller", "One", "seller.one@example.com",
                new HashMap<>());

        CsvUtils.writeProductsToCSV("", seller); // Empty filename

        File file = new File("");
        assertFalse(file.exists());
    }

    @Test
    public void importFromCSV_InvalidFilename() {
        Seller seller = new Seller("sellerUser", "password", "Seller", "One", "seller.one@example.com",
                new HashMap<>());
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            CsvUtils.importFromCSV("", seller, objectMapper); // Empty filename

            // No changes should be made since the filename is invalid
            assertFalse(seller.getStores().containsKey("Electronics"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void importFromCSV_NonExistingFile() {
        Seller seller = new Seller("sellerUser", "password", "Seller", "One", "seller.one@example.com",
                new HashMap<>());
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            CsvUtils.importFromCSV("nonExistingFile", seller, objectMapper);

            // No changes should be made since the file does not exist
            assertFalse(seller.getStores().containsKey("Electronics"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class CsvUtilsTestUtils {
    public static void writeToFile(File file, String[] data) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(file, false))) {
            for (int i = 0; i < data.length; i++) {
                writer.println(data[i]);
            }
        }
    }
}

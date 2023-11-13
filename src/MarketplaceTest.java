import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MarketplaceTest {

    private Marketplace marketplace;
    private ObjectMapper objectMapper;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        System.setOut(new PrintStream(outContent));
        marketplace = new Marketplace(new HashMap<String, Buyer>(), new HashMap<String, Seller>());
        objectMapper = new ObjectMapper();
    }

    private void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    @Test
    public void testAddBuyerAccount() throws IOException {
        Buyer buyer = new Buyer("john_doe", "password123", "John", "Doe", "john.doe@example.com", new HashMap<>(), new HashMap<>());

        // Provide input for scanner
        provideInput("john_doe");

        marketplace.addBuyerAccount("john_doe", buyer, objectMapper);

        Map<String, Buyer> buyers = marketplace.getBuyers();
        assertEquals(1, buyers.size());
        assertEquals(buyer, buyers.get("john_doe"));

        // Verify that the object is added to JSON
        JsonNode rootNode = JsonUtils.readJsonFile(objectMapper);
        JsonNode buyersNode = rootNode.at("/buyers");
        assertTrue(buyersNode.has("john_doe"));
    }

    @Test
    public void testAddSellerAccount() throws IOException {
        Seller seller = new Seller("seller1", "sellerpass", "Seller", "One", "seller@example.com", new HashMap<>());

        // Provide input for scanner
        provideInput("seller1");

        marketplace.addSellerAccount("seller1", seller, objectMapper);

        Map<String, Seller> sellers = marketplace.getSellers();
        assertEquals(1, sellers.size());
        assertEquals(seller, sellers.get("seller1"));

        // Verify that the object is added to JSON
        JsonNode rootNode = JsonUtils.readJsonFile(objectMapper);
        JsonNode sellersNode = rootNode.at("/sellers");
        assertTrue(sellersNode.has("seller1"));
    }

    @Test
    public void testSignInBuyer() throws IOException {
        Buyer buyer = createTestBuyer();
        marketplace.addBuyerAccount("test_buyer", buyer, objectMapper);

        // Provide input for scanner
        provideInput("test_buyer\npassword123");

        Person loggedInUser = marketplace.signIn(new Scanner(System.in));
        assertEquals(buyer, loggedInUser);
    }
    
    @Test
    public void testSignInInvalidCredentials() throws IOException {
        Buyer buyer = createTestBuyer();
        marketplace.addBuyerAccount("test_buyer", buyer, objectMapper);

        // Provide wrong password
        provideInput("test_buyer\nwrong_password\n");
        // invalid input prompts user to try again        
        // then provide corrent ones
        provideInput("test_buyer\npassword123\n");
        Person loggedInUser = marketplace.signIn(new Scanner(System.in));

        assertEquals(buyer, loggedInUser);
    }

    @Test
    public void testSignInSeller() throws IOException {
        Seller seller = createTestSeller();
        marketplace.addSellerAccount("test_seller", seller, objectMapper);

        // Provide input for scanner
        provideInput("test_seller\nsellerpass");

        Person loggedInUser = marketplace.signIn(new Scanner(System.in));
        assertEquals(seller, loggedInUser);
    }

    @Test
    public void testSignUpBuyer() throws IOException {
        // Provide input for scanner
        provideInput("new_buyer\npassword123\nNew\nBuyer\nnew.buyer@example.com\n1");

        Person signedUpUser = marketplace.signUp(new Scanner(System.in), objectMapper);

        assertTrue(signedUpUser instanceof Buyer);
        assertEquals("new_buyer", signedUpUser.getUsername());

        // Verify that the buyer is added to JSON
        JsonNode rootNode = JsonUtils.readJsonFile(objectMapper);
        JsonNode buyersNode = rootNode.at("/buyers");
        assertTrue(buyersNode.has("new_buyer"));
    }

    @Test
    public void testSignUpSeller() throws IOException {
        // Provide input for scanner
        provideInput("new_seller\npassword123\nNew\nSeller\nnew.seller@example.com\n2");

        Person signedUpUser = marketplace.signUp(new Scanner(System.in), objectMapper);

        assertTrue(signedUpUser instanceof Seller);
        assertEquals("new_seller", signedUpUser.getUsername());

        // Verify that the seller is added to JSON
        JsonNode rootNode = JsonUtils.readJsonFile(objectMapper);
        JsonNode sellersNode = rootNode.at("/sellers");
        assertTrue(sellersNode.has("new_seller"));
    }

    // Helper method to create a test buyer
    private Buyer createTestBuyer() {
        return new Buyer("test_buyer", "password123", "Test", "Buyer", "test.buyer@example.com", new HashMap<>(), new HashMap<>());
    }

    // Helper method to create a test seller
    private Seller createTestSeller() {
        return new Seller("test_seller", "sellerpass", "Test", "Seller", "test.seller@example.com", new HashMap<>());
    }
}

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;

public class Marketplace {
    @JsonProperty("Buyers")
    private Map<String, Buyer> buyers;
    @JsonProperty("Sellers")
    private Map<String, Seller> sellers;

    public Map<String, Buyer> getBuyers() {
        return buyers;
    }
    public Map<String, Seller> getSellers() {
        return sellers;
    }


    public void addBuyerAccount(String username, Buyer buyer) {
        buyers.put(username, buyer);
    }

    public void addSellerAccount(String username, Seller seller) {
        sellers.put(username, seller);
    }

    //METHOD TO SIGN IN, RETURN USER OBJECT
    public String signIn(Scanner scanner) {
        while(true) {
            System.out.println("Please enter your username:");
            String username = scanner.nextLine();
            System.out.println("Please enter your password:");
            String password = scanner.nextLine();
            if (buyers.containsKey(username) && buyers.get(username).getPassword().equals(password)) {
                System.out.println("Successfully signed into buyer account...");
                return username;
            } else if (sellers.containsKey(username) && sellers.get(username).getPassword().equals(password)) {
                System.out.println("Successfully signed into seller account...");
                return username;
            } else {
                System.out.println("Sorry, your credentials are invalid. Please try again");
            }
        }
    }

    //METHOD TO SIGN UP, RETURN USER OBJECT
    public String signUp(Scanner scanner, ObjectMapper objectMapper) throws IOException {
        System.out.println("Please enter your username:");
        String username = scanner.nextLine();
        while (buyers.containsKey(username) || sellers.containsKey(username)) {
            System.out.println("Username taken. Please try again:");
            username = scanner.nextLine();
        }
        System.out.println("Please enter your password:");
        String password = scanner.nextLine();
        System.out.println("Please enter your first name:");
        String firstName = scanner.nextLine();
        System.out.println("Please enter your last name:");
        String lastName = scanner.nextLine();
        System.out.println("Would you like to create a buyer (1) or seller (2) account? " +
                "Please enter respective number.");
        String accountType = scanner.nextLine();
        while (!(accountType.equals("1") || accountType.equals("2"))) {
            System.out.println("Invalid. Please enter 1 (buyer) or 2 (seller).");
            accountType = scanner.nextLine();
        }
        if (accountType.equals("1")) {
            Buyer buyer = new Buyer(username, password, firstName, lastName, new HashMap<>(), new HashMap<>());
            addBuyerAccount(username, buyer);
            JsonUtils.addObjectToJson("Buyers", username, buyer, objectMapper);
        } else {
            Seller seller = new Seller(username, password, firstName, lastName, new HashMap<>());
            addSellerAccount(username, seller);
            JsonUtils.addObjectToJson("Sellers", username, seller, objectMapper);
        }
        return username;
    }

    public ArrayList<Item> getAllMarketPlaceItems() {
        ArrayList<Item> itemsArray = new ArrayList<>();;
        for (Map.Entry<String, Seller> sellerEntry : sellers.entrySet()) {
            Seller seller = sellerEntry.getValue();
            Map<String, Store> stores = seller.getStores();
            for (Map.Entry<String, Store> storeEntry : stores.entrySet()) {
                Store store = storeEntry.getValue();
                Map<String, Item> items = store.getStockItems();
                for (Map.Entry<String, Item> itemEntry : items.entrySet()) {
                    itemsArray.add(itemEntry.getValue());
                    System.out.println(itemEntry.getValue().getName());
                }
            }
        }
        return itemsArray;
    }

    public Store getStore(Item item) {
        for (Map.Entry<String, Seller> sellerEntry : sellers.entrySet()) {
            Seller seller = sellerEntry.getValue();
            Map<String, Store> stores = seller.getStores();
            for (Map.Entry<String, Store> storeEntry : stores.entrySet()) {
                Store store = storeEntry.getValue();
                Map<String, Item> items = store.getStockItems();
                for (Map.Entry<String, Item> itemEntry : items.entrySet()) {
                    if (itemEntry.getValue().getName().equals(item.getName())) {
                        return store;
                    }
                }
            }
        }
        return null;
    }
}

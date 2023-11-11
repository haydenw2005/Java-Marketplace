import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;

public class Marketplace {
    @JsonProperty("buyers")
    private Map<String, Buyer> buyers;
    @JsonProperty("sellers")
    private Map<String, Seller> sellers;

    public Map<String, Buyer> getBuyers() {
        return buyers;
    }
    public Map<String, Seller> getSellers() {
        return sellers;
    }


    public void addBuyerAccount(String username, Buyer buyer, ObjectMapper objectMapper) throws IOException {
        buyers.put(username, buyer);
        JsonUtils.addObjectToJson("buyers", username, buyer, objectMapper);
    }

    public void addSellerAccount(String username, Seller seller, ObjectMapper objectMapper) throws IOException{
        sellers.put(username, seller);
        JsonUtils.addObjectToJson("sellers", username, seller, objectMapper);
    }

    //METHOD TO SIGN IN, RETURN USER OBJECT
    public Person signIn(Scanner scanner) {
        while(true) {
            System.out.println("Please enter your username:");
            String username = scanner.nextLine();
            System.out.println("Please enter your password:");
            String password = scanner.nextLine();
            if (buyers.containsKey(username) && buyers.get(username).getPassword().equals(password)) {
                return getBuyers().get(username);
            } else if (sellers.containsKey(username) && sellers.get(username).getPassword().equals(password)) {
                return getSellers().get(username);
            } else {
                System.out.println("Sorry, your credentials are invalid. Please try again");
            }
        }
    }

    //METHOD TO SIGN UP, RETURN USER OBJECT
    public Person signUp(Scanner scanner, ObjectMapper objectMapper) throws IOException {
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
            addBuyerAccount(username, buyer, objectMapper);
            return buyer;
        } else {
            Seller seller = new Seller(username, password, firstName, lastName, new HashMap<>());
            addSellerAccount(username, seller, objectMapper);
            return seller;
        }
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

    public void listProducts(ArrayList<Item> items) {
        System.out.println("Enter the product's number to view its information:");
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + " - " + items.get(i).toString() + " \nStore: "
                    + this.getStore(items.get(i)).getName() + "\n");
        }
    }

    public ArrayList<Item> searchProducts(Scanner scanner, ArrayList<Item> items) {
        ArrayList<Item> searchResults = new ArrayList<>();

        System.out.println("Search for a product: ");
        String search = scanner.nextLine();
        search = search.toUpperCase();

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().toUpperCase().contains(search) ||
                    items.get(i).getDescription().toUpperCase().contains(search) ||
            this.getStore(items.get(i)).getName().toUpperCase().contains(search)) {
                searchResults.add(items.get(i));
            }
        }

        return searchResults;
    }

    public ArrayList<Item> sortByPrice(ArrayList<Item> items) {
        for (int i = 0; i < items.size(); i++) {
            for (int j = i; j < items.size() - 1; j++) {
                if (items.get(i).getPrice() < items.get(j + 1).getPrice()) {
                    Item temp = items.get(i);
                    items.set(i, items.get(j + 1));
                    items.set(j + 1, temp);
                }
            }
        }
        return items;
    }

    public ArrayList<Item> sortByQuantity(ArrayList<Item> items) {
        for (int i = 0; i < items.size(); i++) {
            for (int j = i; j < items.size() - 1; j++) {
                if (items.get(i).getStock() < items.get(j + 1).getStock()) {
                    Item temp = items.get(i);
                    items.set(i, items.get(j + 1));
                    items.set(j + 1, temp);
                }
            }
        }
        return items;
    }

    public Item buyerFlow (Scanner scanner, Buyer buyer) {
        ArrayList<Item> itemsList = this.getAllMarketPlaceItems();
        this.listProducts(itemsList);
        System.out.println("Search for a product: enter 'search'");
        System.out.println("Sort products by price: enter 'sort price'");
        System.out.println("Sort products by quantity in stock: enter 'sort quantity'");

        String input = scanner.nextLine();

        if (input.equals("search")) {
            itemsList = this.searchProducts(scanner, itemsList);
            if (itemsList.size() == 0) {
                System.out.println("No matches");
                this.buyerFlow(scanner, buyer);
            } else {
                this.listProducts(itemsList);
                input = scanner.nextLine();
            }
        } else if (input.equals("sort price")) {
            this.listProducts(this.sortByPrice(itemsList));
            input = scanner.nextLine();
        } else if (input.equals("sort quantity")) {
            this.listProducts(this.sortByQuantity(itemsList));
            input = scanner.nextLine();
        }

        try {
            int inputNum = Integer.parseInt(input);
            if (inputNum >= 1 && inputNum <= itemsList.size()) {
                return this.showProductPage(itemsList.get(inputNum - 1), scanner, buyer);
            } else {
                System.out.println("Invalid input");
                return this.showProductPage(this.buyerFlow(scanner, buyer), scanner, buyer);
            }
        } catch (Exception e) {
            System.out.println("Invalid input");
            return this.showProductPage(this.buyerFlow(scanner, buyer), scanner, buyer);
        }
    }

    public Item showProductPage(Item item, Scanner scanner, Buyer buyer) {
        System.out.println("Product: " + item.getName());
        System.out.println(item.getDescription());
        System.out.println("Quantity available: " + item.getStock());
        System.out.println("Purchase: enter '1'");
        System.out.println("Add to cart: enter '2'");
        System.out.println("Back: enter '3'");
        String input = scanner.nextLine();

        while (!(input.equals("1") || input.equals("2") || input.equals("3"))) {
            System.out.println("invalid input");
            input = scanner.nextLine();
        }

        if (input.equals("1")) {
            System.out.println("How many items would you like to purchase?");
            String s = scanner.nextLine();
            int numItems = 0;
            boolean validInput;
            do {
                validInput = true;
                try {
                    numItems = Integer.parseInt(s);

                    if (numItems <= 0) {
                        System.out.println("Invalid input");
                        System.out.println("How many items would you like to purchase?");
                        s = scanner.nextLine();
                        validInput = false;
                    }

                    if (numItems > item.getStock()) {
                        System.out.println("Not enough items in stock");
                        System.out.println("How many items would you like to purchase?");
                        s = scanner.nextLine();
                        validInput = false;
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input");
                    System.out.println("How many items would you like to purchase?");
                    s = scanner.nextLine();
                    validInput = false;
                }
            } while (!validInput);

            item.setStock(item.getStock() - numItems);
            if (numItems == 1)
                System.out.println(numItems + " item purchased!");
            else
                System.out.println(numItems + " items purchased!");

            // still need to add code to purchase
            
            if (item.getStock() == 0) {
                //remove item from json
                //Need to get seller of
                /*String dir = "/sellers/" + this.getUsername() + "/stores/" + ;
                Store store = JsonUtils.getObjectByKey(objectMapper, dir + "/" + name, Store.class);
                JsonUtils.removeObjectFromJson(dir, name, objectMapper);*/
            }
        } else if (input.equals("2")) {
            buyer.addItemToCart(item, new ObjectMapper());
        } else {
            this.buyerFlow(scanner, buyer);
        }

        return item;
    }

    public void viewStoreInfo(Scanner scanner) {

    }
}

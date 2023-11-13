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
        JsonUtils.addObjectToJson("/buyers", username, buyer, objectMapper);
    }

    public void addSellerAccount(String username, Seller seller, ObjectMapper objectMapper) throws IOException{
        sellers.put(username, seller);
        JsonUtils.addObjectToJson("/sellers", username, seller, objectMapper);
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
        System.out.println("Please enter your permanent username:");
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
        System.out.println("Please enter your email:");
        String email = scanner.nextLine();
        System.out.println("Would you like to create a buyer (1) or seller (2) account? " +
                "Please enter respective number.");
        String accountType = scanner.nextLine();
        while (!(accountType.equals("1") || accountType.equals("2"))) {
            System.out.println("Invalid. Please enter 1 (buyer) or 2 (seller).");
            accountType = scanner.nextLine();
        }
        if (accountType.equals("1")) {
            Buyer buyer = new Buyer(username, password, firstName, lastName, email, new HashMap<>(), new HashMap<>());
            addBuyerAccount(username, buyer, objectMapper);
            return buyer;
        } else {
            Seller seller = new Seller(username, password, firstName, lastName, email, new HashMap<>());
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

    public ArrayList<Store> getAllStores() {
        ArrayList<Store> storesArray = new ArrayList<>();;
        for (Map.Entry<String, Seller> sellerEntry : sellers.entrySet()) {
            Seller seller = sellerEntry.getValue();
            Map<String, Store> stores = seller.getStores();
            for (Map.Entry<String, Store> storeEntry : stores.entrySet()) {
                storesArray.add(storeEntry.getValue());
            }
        }
        return storesArray;
    }


    public Store getStore(Item item) {
        for (Map.Entry<String, Seller> sellerEntry : sellers.entrySet()) {
            Seller seller = sellerEntry.getValue();
            Map<String, Store> stores = seller.getStores();
            for (Map.Entry<String, Store> storeEntry : stores.entrySet()) {
                Store store = storeEntry.getValue();
                Map<String, Item> stockItems = store.getStockItems();
                for (Map.Entry<String, Item> stockItemEntry : stockItems.entrySet()) {
                    if (stockItemEntry.getValue().getName().equals(item.getName())) {
                        return store;
                    }
                Map<String, Item> soldItems = store.getSoldItems();
                for (Map.Entry<String, Item> soldItemEntry : soldItems.entrySet()) {
                    if (soldItemEntry.getValue().getName().equals(item.getName())) {
                        return store;
                    }
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



    public ArrayList<Store> sortByProductsSold(ArrayList<Store> stores) {
        for (int i = 0; i < stores.size(); i++) {
            for (int j = i; j < stores.size() - 1; j++) {
                if (stores.get(i).getNumProductsSold() < stores.get(j + 1).getNumProductsSold()) {
                    Store temp = stores.get(i);
                    stores.set(i, stores.get(j + 1));
                    stores.set(j + 1, temp);
                }
            }
        }
        return stores;
    }


    public ArrayList<Store> sortByProductsBought(ArrayList<Store> stores, Buyer buyer) {
        for (int i = 0; i < stores.size(); i++) {
            for (int j = i; j < stores.size() - 1; j++) {
                if (stores.get(i).getTotalItemsPurchased(this, buyer) >
                        stores.get(j + 1).getTotalItemsPurchased(this, buyer)) {
                    Store temp = stores.get(i);
                    stores.set(i, stores.get(j + 1));
                    stores.set(j + 1, temp);
                }
            }
        }
        return stores;
    }


    public Item showMarketplace (Scanner scanner, Buyer buyer, ObjectMapper objectMapper) {
        ArrayList<Item> itemsList = this.getAllMarketPlaceItems();
        this.listProducts(itemsList);
        System.out.println("\t(a) Search for a product ~");
        System.out.println("\t(b)Sort products by price ~");
        System.out.println("\t(c)Sort products by quantity in stock ~");
        System.out.println("\t(d)Go back ~");

        String input = scanner.nextLine();


        if (input.toLowerCase().equals("a")) {
            itemsList = this.searchProducts(scanner, itemsList);
            if (itemsList.size() == 0) {
                System.out.println("No matches");
                this.showMarketplace(scanner, buyer, objectMapper);
            } else {
                this.listProducts(itemsList);
                input = scanner.nextLine();
            }
        } else if (input.toLowerCase().equals("b")) {
            this.listProducts(this.sortByPrice(itemsList));
            System.out.println("Go back: enter 'back'");
            input = scanner.nextLine();
            if (input.equals("back")) {
                this.showMarketplace(scanner, buyer, objectMapper);
            }
        } else if (input.toLowerCase().equals("c")) {
            this.listProducts(this.sortByQuantity(itemsList));
            System.out.println("Go back: enter 'back'");
            input = scanner.nextLine();
            if (input.equals("back")) {
                this.showMarketplace(scanner, buyer, objectMapper);
            }
        } else if (input.toLowerCase().equals("d")) {
            return null;
        }

        try {
            int inputNum = Integer.parseInt(input);
            if (inputNum >= 1 && inputNum <= itemsList.size()) {
                return this.showProductPage(itemsList.get(inputNum - 1), scanner, buyer, objectMapper);
            } else {
                System.out.println("Invalid input");
                return this.showProductPage(this.showMarketplace(scanner, buyer, objectMapper), scanner, buyer, objectMapper);
            }
        } catch (Exception e) {
            System.out.println("Invalid input");
            return this.showProductPage(this.showMarketplace(scanner, buyer, objectMapper), scanner, buyer, objectMapper);
        }
    }


    public Item showProductPage(Item item, Scanner scanner, Buyer buyer, ObjectMapper objectMapper) {
        System.out.println("Product: " + item.getName());
        System.out.println("Description: " + item.getDescription());
        System.out.println("Quantity available: " + item.getStock());
        System.out.println("\t(1) Purchase");
        System.out.println("\t(2) Add to Cart");
        System.out.println("\t(3) Back");
        String input = scanner.nextLine();


        while (!(input.equals("1") || input.equals("2") || input.equals("3"))) {
            System.out.println("Invalid input.");
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

            item.setCount(numItems);
            if (numItems == 1)
                System.out.println(numItems + " item purchased!");
            else
                System.out.println(numItems + " items purchased!");
            
            buyer.buyItem(item, this, objectMapper); // handles stock updating/cart removing etc.
            return item;
        } else if (input.equals("2")) {
            buyer.addItemToCart(item, objectMapper);
        } else {
            this.showMarketplace(scanner, buyer, objectMapper);
        }


        return item;
    }


    public void viewStoreInfo(Scanner scanner, Buyer buyer, ObjectMapper objectMapper) {
        ArrayList<Store> allStores = this.getAllStores();


        System.out.println("\t(1) View stores by number of products sold");
        System.out.println("\t(2) View stores by products you purchased");
        System.out.println("\t(3) Back");


        String input = scanner.nextLine();
        while (!(input.equals("1") || input.equals("2") || input.equals("3"))) {
            System.out.println("Invalid input");
            input = scanner.nextLine();
        }


        if (input.equals("1")) {
            this.listStoresBySales(scanner, allStores, buyer, objectMapper);
        } else if (input.equals("2")) {
            this.listStoresByProductsBought(scanner, allStores, buyer, objectMapper);
        } else {
            return;
        }


    }


    public void listStoresBySales(Scanner scanner, ArrayList<Store> allStores, Buyer buyer, ObjectMapper objectMapper) {
        for (int i = 0; i < allStores.size(); i++) {
            System.out.println(allStores.get(i).getName() + " | Products sold: " +
                    allStores.get(i).getNumProductsSold());
        }


        System.out.println("\t(1) Sort stores by number of products sold");
        System.out.println("\t(2) Back");


        String input = scanner.nextLine();
        while (!(input.equals("1") || input.equals("2"))) {
            System.out.println("Invalid input");
            input = scanner.nextLine();
        }


        if (input.equals("1")) {
            listStoresBySales(scanner, this.sortByProductsSold(allStores), buyer, objectMapper);
        } else {
            this.viewStoreInfo(scanner, buyer, objectMapper);
        }
    }




    public void listStoresByProductsBought(Scanner scanner, ArrayList<Store> allStores, Buyer buyer, ObjectMapper objectMapper) {
        ArrayList<ArrayList<Item>> list = new ArrayList<ArrayList<Item>>();
        for (int i = 0; i < allStores.size(); i++) {
            list.add(allStores.get(i).getProductsPurchasedFromStore(this, buyer));
        }
        
        for (int i = 0; i < allStores.size(); i++) {
            if (list.get(i).size() > 0) {
                System.out.println("From " + allStores.get(i).getName() + ", you purchased:");
                for (int j = 0; j < list.get(i).size(); j++) {
                    System.out.println("\t" + list.get(i).get(j).getCount() + " " + list.get(i).get(j).getName());
                }
            }
        }


        System.out.println("\t(1) Sort stores by number of products bought");
        System.out.println("\t(2) Back");


        String input = scanner.nextLine();
        while (!(input.equals("1") || input.equals("2"))) {
            System.out.println("Invalid input");
            input = scanner.nextLine();
        }


        if (input.equals("1")) {
            listStoresByProductsBought(scanner, this.sortByProductsBought(allStores, buyer), buyer, objectMapper);


        } else {
            this.viewStoreInfo(scanner, buyer, objectMapper);
        }
    }


    public void cartFlow(Scanner scanner, Buyer buyer, ObjectMapper objectMapper) {
        buyer.showAllCartItems();
        System.out.println("\n\t(1) Buy Cart");
        System.out.println("\t(2) Back");
        String input = scanner.nextLine();
        while (!(input.equals("1") || input.equals("2"))) {
            System.out.println("Invalid input. Try again.");
            input = scanner.nextLine();
        }

        if (input.equals("1")) {
            buyer.buyCart(this, objectMapper);
            return;
        } else if (input.equals("2")) {
            return;
        } 

    }

    public void editUser(Scanner scanner, Person user, ObjectMapper objectMapper) {
        System.out.println("Please enter your new password:");
        String password = scanner.nextLine();
        System.out.println("Please enter your new first name:");
        String firstName = scanner.nextLine();
        System.out.println("Please enter your new last name:");
        String lastName = scanner.nextLine();
        System.out.println("Please enter your new email:");
        String email = scanner.nextLine();
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        try {
            if (user instanceof Buyer) {
                addBuyerAccount(user.getUsername(), (Buyer) user, objectMapper);
            } else {
                addSellerAccount(user.getUsername(), (Seller) user, objectMapper);
            }
        } catch (Exception e) {
            System.out.println("There was an error updating your account.");
        }

    }
}

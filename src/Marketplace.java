import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.*;

/**
 * Represents a marketplace where buyers and sellers interact to buy and sell
 * items.
 * Provides methods for managing accounts, signing in, signing up, and
 * interacting with the marketplace. Contains all user flow.
 *
 * <p>
 * Purdue University -- CS18000 -- Fall 2023
 * </p>
 *
 * @author Hayden, Soham, and Ryan
 * @version November 13, 2023
 */
public class Marketplace {
    @JsonProperty("buyers")
    private Map<String, Buyer> buyers;
    @JsonProperty("sellers")
    private Map<String, Seller> sellers;

    public Marketplace() { }
    public Marketplace(@JsonProperty("buyers") Map<String, Buyer> buyers, 
            @JsonProperty("sellers") Map<String, Seller> sellers) {
        this.buyers = buyers;
        this.sellers = sellers;
    }

    public Map<String, Buyer> getBuyers() {
        return buyers;
    }

    public Map<String, Seller> getSellers() {
        return sellers;
    }

    public Person enterCredentials(Scanner scanner, ObjectMapper objectMapper) throws IOException {
        System.out.println("Welcome to zBay Market Place! (FULL SCREEN RECOMMENDED)");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Would you like to sign in (1) or sign up (2)?");
        String response = scanner.nextLine();
        Person user = null;
        while (!(response.equals("1") || response.equals("2"))) {
            System.out.println("Invalid. Please enter 1 (sign in) or 2 (sign up).");
            response = scanner.nextLine();
        }
        if (response.equals("1")) {
            user = signIn(scanner);
        } else {
            user = signUp(scanner, objectMapper);
        }
        return user;
    }

    public void addBuyerAccount(String username, Buyer buyer, ObjectMapper objectMapper) throws IOException {
        buyers.put(username, buyer);
        JsonUtils.addObjectToJson("/buyers", username, buyer, objectMapper);
    }

    public void addSellerAccount(String username, Seller seller, ObjectMapper objectMapper) throws IOException {
        sellers.put(username, seller);
        JsonUtils.addObjectToJson("/sellers", username, seller, objectMapper);
    }

    // METHOD TO SIGN IN, RETURN USER OBJECT
    public Person signIn(Scanner scanner) {
        while (true) {
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

    // METHOD TO SIGN UP, RETURN USER OBJECT
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

    public void startBuyerFlow(Buyer user, Scanner scanner, ObjectMapper objectMapper) {
        System.out.println("Welcome " + user.getFirstName() + " " + user.getLastName() + "!");
        while (true) {
            System.out.println("What would you like to do?");
            System.out.println("\t(1) View store information");
            System.out.println("\t(2) View marketplace");
            System.out.println("\t(3) View cart");
            System.out.println("\t(4) View purchase history");
            System.out.println("\t(5) Export purchase history to CSV");
            System.out.println("\t(6) Edit account");
            System.out.println("\t(7) Delete account");
            System.out.println("\t(8) Signout");

            String input = scanner.nextLine();
            while (!(input.equals("1") || input.equals("2") || input.equals("3")
                    || input.equals("4") || input.equals("5") || input.equals("6")
                    || input.equals("7") || input.equals("8"))) {
                System.out.println("Invalid input");
                input = scanner.nextLine();
            }
            if (input.equals("1")) {
                viewStoreInfo(scanner, (Buyer) user, objectMapper);
            } else if (input.equals("2")) {
                showMarketplace(scanner, (Buyer) user, objectMapper);
            } else if (input.equals("3")) {
                cartFlow(scanner, (Buyer) user, objectMapper);
            } else if (input.equals("4")) {
                user.showPurchaseHistory();
            } else if (input.equals("5")) {
                try {
                    System.out.println("Enter filename to export to (excluding .csv extension)");
                    String file = scanner.nextLine();
                    CsvUtils.writePurchaseHistoryToCSV(file, (Buyer) user);
                } catch (Exception e) {
                    System.out.println("An error occurred while writing to file.");
                }
            } else if (input.equals("6")) {
                System.out.println("Edit account ~");
                editUser(scanner, user, objectMapper);
                System.out.println();
            } else if (input.equals("7")) {
                System.out.println("Deleting account...");
                deleteUser(user, objectMapper);
                break;
            } else if (input.equals("8")) {
                System.out.println("Signing out...");
                break;
            }
        }
    }


    public ArrayList<Item> getAllMarketPlaceItems() {
        ArrayList<Item> itemsArray = new ArrayList<>();
        ;
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
        ArrayList<Store> storesArray = new ArrayList<>();
        ;
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
                    if (stockItemEntry.getValue().equals(item)) {
                        return store;
                    }
                    Map<String, Item> soldItems = store.getSoldItems();
                    for (Map.Entry<String, Item> soldItemEntry : soldItems.entrySet()) {
                        if (soldItemEntry.getValue().equals(item)) {
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
                if (stores.get(i).numProductsSold() < stores.get(j + 1).numProductsSold()) {
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
                if (stores.get(i).getTotalItemsPurchased(this, buyer) > stores.get(j + 1).getTotalItemsPurchased(this,
                        buyer)) {
                    Store temp = stores.get(i);
                    stores.set(i, stores.get(j + 1));
                    stores.set(j + 1, temp);
                }
            }
        }
        return stores;
    }

    public Item showMarketplace(Scanner scanner, Buyer buyer, ObjectMapper objectMapper) {
        ArrayList<Item> itemsList = this.getAllMarketPlaceItems();
        this.listProducts(itemsList);
        System.out.println("\t (a) Search for a product ~");
        System.out.println("\t (b) Sort products by price ~");
        System.out.println("\t (c) Sort products by quantity in stock ~");
        System.out.println("\t (d) Go back ~");

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
        if (input.equals("back"))
            return null;

        try {
            int inputNum = Integer.parseInt(input);
            if (inputNum >= 1 && inputNum <= itemsList.size()) {
                return this.showProductPage(itemsList.get(inputNum - 1), scanner, buyer, objectMapper);
            } else {
                System.out.println("Invalid input");
                return this.showProductPage(this.showMarketplace(scanner, buyer, objectMapper), scanner, buyer,
                        objectMapper);
            }
        } catch (Exception e) {
            System.out.println("Invalid input");
            return this.showProductPage(this.showMarketplace(scanner, buyer, objectMapper), scanner, buyer,
                    objectMapper);
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
        System.out.println("What would you like to do?");
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
                    allStores.get(i).numProductsSold());
        }

        System.out.println("What would you like to do?");
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

    public void listStoresByProductsBought(Scanner scanner, ArrayList<Store> allStores, Buyer buyer,
            ObjectMapper objectMapper) {
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
        System.out.println("What would you like to do?");
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

    public void deleteUser(Person user, ObjectMapper objectMapper) {
        try {
            if (user instanceof Buyer) {
                String dir = "/buyers";
                JsonUtils.removeObjectFromJson(dir, user.getUsername(), objectMapper);
            } else {
                String dir = "/sellers";
                JsonUtils.removeObjectFromJson(dir, user.getUsername(), objectMapper);

            }
        } catch (Exception e) {
            System.out.println("Error deleting account");
        }
    }

    public void startSellerFlow(Seller user, Scanner scanner, ObjectMapper objectMapper) {
        System.out.println("Welcome " + user.getFirstName() + " " + user.getLastName() + "!");
        while (true) {
            printSellerMenu();
            String option = getMenuInput(1, 10, scanner);
            switch (option) {
                case "1":
                    boolean inItemMenu = true;
                    while (inItemMenu) {
                        printSellerItemMenu();
                        try {
                            inItemMenu = getItemMenuInput(scanner, user, objectMapper);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        user = updatedSeller(objectMapper, user.getUsername());
                    }
                    System.out.println();
                    break;
                case "2":
                    boolean inStoreMenu = true;
                    while (inStoreMenu) {
                        printSellerStoreMenu();
                        try {
                            inStoreMenu = getStoreMenuInput(scanner, user, objectMapper);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        user = updatedSeller(objectMapper, user.getUsername());
                    }
                    System.out.println();
                    break;
                case "3":
                    System.out.println("All listed products");
                    ArrayList<Item> stockItems = user.getAllStoreItems("stock");
                    for (Item item : stockItems) {
                        System.out.println(item.toString());
                    }
                    System.out.println();
                    break;
                case "4":
                    System.out.println("All sold products");
                    ArrayList<Item> soldItems = user.getAllStoreItems("sold");
                    for (Item item : soldItems) {
                        System.out.println(item.toString());
                    }
                    System.out.println();
                    break;
                case "5":
                    System.out.println("All product buyers");
                    HashMap<String, Integer> buyers = user.allBuyers();
                    for (Map.Entry<String, Integer> entry : buyers.entrySet()) {
                        String key = entry.getKey();
                        Integer value = entry.getValue();
                        System.out.println("\tUser " + key + " has bought " + value + " products ~");
                    }
                    System.out.println();
                    break;
                case "6":
                    System.out.println("Enter filename to write to (excluding .csv extension)");
                    String file = scanner.nextLine();
                    try {
                        CsvUtils.writeProductsToCSV(file, (Seller) user);
                    } catch (Exception e) {
                        System.out.println("Error writing to file.");
                        ;
                    }
                    break;
                case "7":
                    System.out.println("Enter filename to read from (excluding .csv extension)");
                    String filename = scanner.nextLine();
                    try {
                        CsvUtils.importFromCSV(filename, (Seller) user, objectMapper);
                    } catch (Exception e) {
                        System.out.println("Error reading from file.");
                        e.printStackTrace();
                    }
                    break;
                case "8":
                    System.out.println("Edit account");
                    editUser(scanner, user, objectMapper);
                    System.out.println();
                    user = updatedSeller(objectMapper, user.getUsername());
                    break;
                case "9":
                    System.out.println("Deleting account...");
                    deleteUser(user, objectMapper);
                    return;
                case "10":
                    System.out.println("Signing out...");
                    return;
            }
        }
    }

    public String getMenuInput(int start, int end, Scanner scanner) {
        System.out.println("What would you like to do next? Please enter a number " + start + " through " + end + ":");
        while (true) {
            String option = scanner.nextLine();
            try {
                int input = Integer.parseInt(option);
                if (input >= start && input <= end) {
                    return option;
                } else
                    System.out.println("Invalid input. Please enter a number from " + start + " to " + end + ":");
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number:");
            }
        }
    }

    public boolean getStoreMenuInput(Scanner scanner, Seller user, ObjectMapper objectMapper)
            throws IOException {
        String option = getMenuInput(1, 4, scanner);
        switch (option) {
            case "1":
                System.out.println("What would you like the name of the store to be?");
                String name = scanner.nextLine();
                if (!user.getStores().containsKey(name))
                    user.createNewStore(name, objectMapper);
                else
                    System.out.println("Sorry, you already have a store with this name.");
                break;
            case "2":
                System.out.println("What is the name of the store you would like to edit?");
                String storeName = scanner.nextLine();
                if (user.getStores().containsKey(storeName)) {
                    System.out.println("What will the new name of your store be?");
                    String newStoreName = scanner.nextLine();
                    user.editStore(storeName, newStoreName, objectMapper);
                } else
                    System.out.println("Sorry, we can't find a store with name " + storeName);
                break;
            case "3":
                System.out.println("What store would you like to delete");
                String deletedStoreName = scanner.nextLine();
                if (user.getStores().containsKey(deletedStoreName)) {
                    String dir = "/sellers/" + user.getUsername() + "/stores";
                    JsonUtils.removeObjectFromJson(dir, deletedStoreName, objectMapper);
                } else
                    System.out.println("Sorry, we can't find a store with name " + deletedStoreName);
                break;
            case "4":
                return false;
        }
        return true;
    }

    public boolean getItemMenuInput(Scanner scanner, Seller user, ObjectMapper objectMapper) throws IOException {
        String option = getMenuInput(1, 4, scanner);
        switch (option) {

            case "1":
                Item newItem = createItem(scanner, user);
                while (true) {
                    System.out.println("What is the name of the store you would like to list it in?");
                    String storeName = scanner.nextLine();
                    if (user.getStores().containsKey(storeName)) {
                        user.getStoreByName(storeName).addToStockItems(newItem, user.getUsername(), objectMapper);
                        break;
                    } else
                        System.out.println("Sorry, we can't find a store with this name.");
                }
                break;
            case "2":
                while (true) {
                    System.out.println("What is the name of the item you would like to restock?");
                    String itemName = scanner.nextLine();
                    Item itemToChange = getItemFromAllStores(itemName, user);
                    if (itemToChange != null) {
                        Store store = user.getStoreByItem(itemToChange);
                        int stock = (int) getValidDouble(scanner, "What would you like the item stock to be?");
                        itemToChange.setStock(stock);
                        String dir = "/sellers/" + user.getUsername() + "/stores/" + store.getName() + "/stockItems";
                        JsonUtils.addObjectToJson(dir, itemName, itemToChange, objectMapper);
                        break;
                    } else
                        System.out.println("Sorry, we can't find an item with this name.");
                }
                break;
            case "3":
                System.out.println("What item would you like to delete");
                String deletedItemName = scanner.nextLine();
                Item itemToDelete = getItemFromAllStores(deletedItemName, user);
                if (itemToDelete != null) {
                    user.getStoreByItem(itemToDelete).deleteItem(user.getUsername(), deletedItemName, objectMapper);
                } else
                    System.out.println("Sorry, we can't find an item with name " + deletedItemName);
                break;
            case "4":
                return false;
        }
        return true;
    }

    public Item getItemFromAllStores(String itemName, Seller user) {
        ArrayList<Item> items = user.getAllStoreItems("stock");
        for (Item item : items) {
            if (item.getName().equals(itemName)) {
                return item;
            }
        }
        return null;

    }

    public double getValidDouble(Scanner scanner, String message) {
        while (true) {
            System.out.println(message);
            try {
                double input = Double.parseDouble(scanner.nextLine());
                if (input <= 0) {
                    System.out.println("Please enter a number greater than 0.");
                } else
                    return input;
            } catch (Exception e) {
                System.out.println("Please enter a valid number.");
            }
        }

    }

    public Item createItem(Scanner scanner, Seller user) {

        String name;
        while (true) {
            System.out.println("Please enter a name for the product:");
            name = scanner.nextLine();
            boolean doesExist = getItemFromAllStores(name, user) != null;
            if (!doesExist)
                break;
            else
                System.out.println("Sorry, this item already exists. Try another name.");
        }
        System.out.println("Please enter a description for the product:");
        String description = scanner.nextLine();
        int stock = (int) getValidDouble(scanner, "Please enter how many of these items you would like to list:");
        double price = getValidDouble(scanner, "Please enter the price of the item:");
        HashMap<String, Integer> sellerHashmap = new HashMap<String, Integer>();
        sellerHashmap.put(user.getUsername(), stock);
        return new Item(name, description, stock, -1, price, null, sellerHashmap);
    }

    public void printSellerItemMenu() {
        System.out.println("What would you like to do?");
        System.out.println("\t(1) List new item ~");
        System.out.println("\t(2) Restock items ~");
        System.out.println("\t(3) Delete items ~");
        System.out.println("\t(4) Back");
    }

    public void printSellerStoreMenu() {
        System.out.println("What would you like to do?");
        System.out.println("\t(1) Create a store ~");
        System.out.println("\t(2) Edit a store ~");
        System.out.println("\t(3) Delete a store ~");
        System.out.println("\t(4) Back");
    }

    public void printSellerMenu() {
        System.out.println("What would you like to do?");
        System.out.println("\t(1) List, edit, or delete items ~");
        System.out.println("\t(2) Create, edit, or delete stores ~");
        System.out.println("\t(3) View listed products dashboard ~");
        System.out.println("\t(4) View all sold products dashboard ~");
        System.out.println("\t(5) View all product buyers dashboard ~");
        System.out.println("\t(6) Export store items to CSV ~");
        System.out.println("\t(7) Import store items from CSV ~");
        System.out.println("\t(8) Edit account");
        System.out.println("\t(9) Delete account");
        System.out.println("\t(10) Sign-out ~");
    }

    public static Seller updatedSeller(ObjectMapper objectMapper, String username) {
        try {
            String dir = "/sellers/" + username;
            return JsonUtils.objectByKey(objectMapper, dir, Seller.class);
        } catch (Exception e) {
            System.out.println("Sorry, could not update user.");
            return null;
        }
    }
}

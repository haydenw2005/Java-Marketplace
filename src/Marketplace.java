import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import javax.swing.*;

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
public class Marketplace implements Serializable {
    @JsonProperty("buyers")
    private Map<String, Buyer> buyers;
    @JsonProperty("sellers")
    private Map<String, Seller> sellers;

    public Marketplace() {
    }

    public Marketplace(@JsonProperty("buyers") Map<String, Buyer> buyers,
            @JsonProperty("sellers") Map<String, Seller> sellers) {
        this.buyers = buyers;
        this.sellers = sellers;
    }

    public void updateMarketPlace(Marketplace marketplace) {
        this.buyers = marketplace.buyers;
        this.sellers = marketplace.sellers;
    }

    public Map<String, Buyer> getBuyers() {
        return buyers;
    }

    public Map<String, Seller> getSellers() {
        return sellers;
    }

    public Person enterCredentials(ObjectMapper objectMapper) throws IOException {
        String[] options = { "Sign in", "Sign up" };
        int response;
        boolean error;
        do {
            error = false;
            response = JOptionPane.showOptionDialog(null, "Welcome to zBay Marketplace!",
                    "Welcome", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, options, options[0]);

            if (!(response == 0 || response == 1)) {
                JOptionPane.showMessageDialog(null, "Please choose to sign in or sign up",
                        "Error", JOptionPane.ERROR_MESSAGE);
                error = true;
            }
        } while (error);

        Person user;
        if (response == 0) {
            user = signIn();
        } else {
            user = signUp(objectMapper);
        }

        JOptionPane.showMessageDialog(null, "Welcome " + user.getFirstName() +
                " " + user.getLastName() + "!", "Welcome", JOptionPane.INFORMATION_MESSAGE);

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
    public Person signIn() {
        while (true) {
            String username = JOptionPane.showInputDialog(null, "Please enter your username:",
                    "zBay Marketplace", JOptionPane.QUESTION_MESSAGE);
            String password = JOptionPane.showInputDialog(null, "Please enter your password:",
                    "zBay Marketplace", JOptionPane.QUESTION_MESSAGE);
            if (buyers.containsKey(username) && buyers.get(username).getPassword().equals(password)) {
                return getBuyers().get(username);
            } else if (sellers.containsKey(username) && sellers.get(username).getPassword().equals(password)) {
                return getSellers().get(username);
            } else {
                JOptionPane.showMessageDialog(null, "Your credentials are invalid. Please try again",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // METHOD TO SIGN UP, RETURN USER OBJECT
    public Person signUp(ObjectMapper objectMapper) throws IOException {
        boolean error;
        String username;
        do {
            error = false;
            username = JOptionPane.showInputDialog(null, "Please enter your permanent username:",
                    "zBay Marketplace", JOptionPane.QUESTION_MESSAGE);

            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a valid username",
                        "Error", JOptionPane.ERROR_MESSAGE);
                error = true;
            }

            if (buyers.containsKey(username) || sellers.containsKey(username)) {
                JOptionPane.showMessageDialog(null, "Username taken. Please try again",
                        "Error", JOptionPane.ERROR_MESSAGE);
                error = true;
            }
        } while (error);

        String password;
        do {
            error = false;
            password = JOptionPane.showInputDialog(null, "Please enter your password:",
                    "zBay Marketplace", JOptionPane.QUESTION_MESSAGE);

            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a valid password",
                        "Error", JOptionPane.ERROR_MESSAGE);
                error = true;
            }
        } while (error);

        String firstName;
        do {
            error = false;
            firstName = JOptionPane.showInputDialog(null, "Please enter your first name:",
                    "zBay Marketplace", JOptionPane.QUESTION_MESSAGE);

            if (firstName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a valid name",
                        "Error", JOptionPane.ERROR_MESSAGE);
                error = true;
            }
        } while (error);

        String lastName;
        do {
            error = false;
            lastName = JOptionPane.showInputDialog(null, "Please enter your last name:",
                    "zBay Marketplace", JOptionPane.QUESTION_MESSAGE);

            if (lastName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a valid name",
                        "Error", JOptionPane.ERROR_MESSAGE);
                error = true;
            }
        } while (error);

        String email;
        do {
            error = false;
            email = JOptionPane.showInputDialog(null, "Please enter your email:",
                    "zBay Marketplace", JOptionPane.QUESTION_MESSAGE);

            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a valid email",
                        "Error", JOptionPane.ERROR_MESSAGE);
                error = true;
            }
        } while (error);

        String[] options = { "Buyer", "Seller" };
        int accountType;
        do {
            error = false;
            accountType = JOptionPane.showOptionDialog(null, "What type of account would you like to create?",
                    "Welcome", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, options, options[0]);

            if (!(accountType == 0 || accountType == 1)) {
                JOptionPane.showMessageDialog(null, "Please choose an account type",
                        "Error", JOptionPane.ERROR_MESSAGE);
                error = true;
            }
        } while (error);

        if (accountType == 0) {
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
        ArrayList<Item> itemsArray = new ArrayList<>();
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
                    if (stockItemEntry.getValue().equalsItem(item)) {
                        return store;
                    }
                    Map<String, Item> soldItems = store.getSoldItems();
                    for (Map.Entry<String, Item> soldItemEntry : soldItems.entrySet()) {
                        if (soldItemEntry.getValue().equalsItem(item)) {
                            return store;
                        }
                    }
                }
            }
        }
        return null;
    }

    public ArrayList<Item> searchProducts(String search, ArrayList<Item> items) {
        ArrayList<Item> searchResults = new ArrayList<>();
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
                if (stores.get(i).getTotalItemsPurchased(this, buyer) < stores.get(j + 1).getTotalItemsPurchased(this,
                        buyer)) {
                    Store temp = stores.get(i);
                    stores.set(i, stores.get(j + 1));
                    stores.set(j + 1, temp);
                }
            }
        }
        return stores;
    }

    public void editUser(Person user, ObjectMapper objectMapper, ObjectOutputStream oos, ObjectInputStream ois) {
        String password = JOptionPane.showInputDialog(null,
                "Please enter your new password:",
                "zBay Marketplace", JOptionPane.QUESTION_MESSAGE);
        if (password == null)
            return;
        String firstName = JOptionPane.showInputDialog(null,
                "Please enter your new first name:",
                "zBay Marketplace", JOptionPane.QUESTION_MESSAGE);
        if (firstName == null)
            return;
        String lastName = JOptionPane.showInputDialog(null,
                "Please enter your new last name:",
                "zBay Marketplace", JOptionPane.QUESTION_MESSAGE);
        if (lastName == null)
            return;
        String email = JOptionPane.showInputDialog(null,
                "Please enter your new email:",
                "zBay Marketplace", JOptionPane.QUESTION_MESSAGE);
        if (email == null)
            return;

        if (password.isEmpty() || firstName.isEmpty() ||
                lastName.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "One or more fields were empty",
                    "Error", JOptionPane.ERROR_MESSAGE);
            this.editUser(user, objectMapper, oos, ois);
        }
        try {
            oos.writeObject("editUser");
            oos.writeObject(new Object[] { password, firstName, lastName, email });
            oos.flush();
            user = (Person) ois.readObject();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "There was an error updating your account",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteUser(Person user, ObjectMapper objectMapper, ObjectOutputStream oos, ObjectInputStream ois) {
        try {
            oos.writeObject("deleteUser");
            oos.flush();

            JOptionPane.showMessageDialog(null, "Account deleted",
                    "Success!", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error deleting account",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
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

    public void createItem(Seller user, ObjectMapper objectMapper, ObjectOutputStream oos, ObjectInputStream ois) {
        boolean error = false;
        String storeName = storePicker(user, null).getName();
        if (storeName == null) {
            return;
        }

        String itemName;
        do {
            error = false;
            itemName = JOptionPane.showInputDialog(null,
                    "Enter the item name:",
                    "zBay Marketplace", JOptionPane.QUESTION_MESSAGE);

            if (itemName == null || itemName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Invalid item name",
                        "Error", JOptionPane.ERROR_MESSAGE);
                error = true;
                return;
            }
        } while (error);

        String description;
        do {
            error = false;
            description = JOptionPane.showInputDialog(null,
                    "Enter the item's description:",
                    "zBay Marketplace", JOptionPane.QUESTION_MESSAGE);

            if (description == null || description.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Invalid description",
                        "Error", JOptionPane.ERROR_MESSAGE);
                error = true;
                return;
            }
        } while (error);

        String stockString;
        do {
            error = false;
            stockString = JOptionPane.showInputDialog(null,
                    "Enter the number of items you would like to list:",
                    "zBay Marketplace", JOptionPane.QUESTION_MESSAGE);

            if (stockString == null || stockString.isEmpty() || !stockString.chars().allMatch(Character::isDigit)) {
                JOptionPane.showMessageDialog(null, "Invalid entry",
                        "Error", JOptionPane.ERROR_MESSAGE);
                error = true;
                return;
            }
        } while (error);

        String priceString;
        do {
            error = false;
            priceString = JOptionPane.showInputDialog(null,
                    "Enter the price of this item:",
                    "zBay Marketplace", JOptionPane.QUESTION_MESSAGE);

            if (priceString == null || priceString.isEmpty() || !priceString.chars().allMatch(Character::isDigit)) {
                JOptionPane.showMessageDialog(null, "Invalid entry",
                        "Error", JOptionPane.ERROR_MESSAGE);
                error = true;
            }
        } while (error);

        try {
            int stock = Integer.parseInt(stockString);
            double price = Double.parseDouble(priceString);

            oos.writeObject("createItem");
            oos.writeObject(new Object[] { itemName, description, stock, price, storeName });
            oos.flush();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "One of your entries was invalid",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
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

    private Store storePicker(Seller seller, JFrame frame) {
        ArrayList<String> storeNames = new ArrayList<String>();
        ArrayList<Store> stores = new ArrayList<Store>();

        for (Map.Entry<String, Store> store : seller.getStores().entrySet()) {
            storeNames.add(store.getValue().getName());
            stores.add(store.getValue());
        }
        JComboBox<String> storeComboBox = new JComboBox<String>(storeNames.toArray(new String[0]));
        int result = JOptionPane.showConfirmDialog(frame, storeComboBox, "Select a Store", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Store selectedStore = stores.get(storeComboBox.getSelectedIndex());
            return selectedStore;
        }
        return new Store();
    }
}

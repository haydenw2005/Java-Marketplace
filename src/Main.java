import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.text.MaskFormatter;


public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            //Example of getObjectByKey
            Marketplace marketplace = JsonUtils.getObjectByKey(objectMapper, "", Marketplace.class);
            //System.out.println(buyer);

            //START OF USER FLOW
            Person user = enterCredentials(scanner, objectMapper);
            if (user instanceof Buyer){
                startBuyerFlow((Buyer) user, marketplace, scanner, objectMapper);

            } else if (user instanceof Seller) {
                startSellerFlow((Seller) user, marketplace, scanner, objectMapper);
            }
            //by tracking sessionUsername we know who is using the system
            //System.out.println(sessionUsername);
            //CONTINUE USER FLOW HERE

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Person enterCredentials(Scanner scanner, ObjectMapper objectMapper) throws IOException {
        Marketplace marketplace = JsonUtils.getObjectByKey(objectMapper, "", Marketplace.class);
        System.out.println("Welcome. Would you like to sign in (1) or sign up (2)?");
        String response = scanner.nextLine();
        Person user = null;
        while (!(response.equals("1") || response.equals("2"))) {
            System.out.println("Invalid. Please enter 1 (sign in ) or 2 (sign up).");
            response = scanner.nextLine();
        }
        if (response.equals("1")) {
            user = marketplace.signIn(scanner);
        } else {
            user = marketplace.signUp(scanner, objectMapper);
        }
        return user;
    }
    
    public static void startBuyerFlow(Buyer user, Marketplace marketplace, Scanner scanner, ObjectMapper objectMapper) {
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
            || input.equals("4")|| input.equals("5"))) {
                System.out.println("Invalid input");
                input = scanner.nextLine();
            }
            if (input.equals("1")) {
                marketplace.viewStoreInfo(scanner, (Buyer) user, objectMapper);
            } else if (input.equals("2")) {
                marketplace.showMarketplace(scanner, (Buyer) user, objectMapper);
            } else if (input.equals("3")) {
                marketplace.cartFlow(scanner, (Buyer) user, objectMapper);
            } else if (input.equals("4")) {
                ((Buyer) user).showPurchaseHistory();
                continue;
            } else if (input.equals("5")) {
                try {
                    System.out.println("Enter filename to export to (excluding .csv extension)");
                    String file = scanner.nextLine();
                    CsvUtils.writePurchaseHistoryToCSV(file, (Buyer) user);
                } catch (IOException e) {
                    System.out.println("An error occured while writing to file.");
                }
            } else if (input.equals("6")) {
                System.out.println("Edit account");
                marketplace.editUser(scanner, user, objectMapper);
                System.out.println();
                break;
            } else if (input.equals("7")) {
                System.out.println("Deleting account...");
                break;
            } else if (input.equals("8")) {
                System.out.println("Signing out...");
                break;
            }
        }
    }
    public static void startSellerFlow(Seller user, Marketplace marketplace, Scanner scanner, ObjectMapper objectMapper) {
        System.out.println("Welcome " + user.getFirstName() + " " + user.getLastName() + "!");
        while (true) {
            printSellerMenu();
            String option = getMenuInput(1, 9, scanner);
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
                    HashMap<String, Integer> buyers = user.getAllBuyers();
                    for (Map.Entry<String, Integer> entry : buyers.entrySet()) {
                        String key = entry.getKey();
                        Integer value = entry.getValue();
                        System.out.println("\tUser " + key + " has bought " + value + "products ~");
                    }
                    System.out.println();
                    break;
                case "6":
                    System.out.println("Edit account");
                    marketplace.editUser(scanner, user, objectMapper);
                    System.out.println();
                    return;
                case "7":
                    System.out.println("Deleting account...");
                    return;
                case "8":
                    System.out.println("Enter filename to write to (excluding .csv extension)");
                    String file = scanner.nextLine();
                    try {
                        CsvUtils.writeProductsToCSV(file, (Seller) user);
                    } catch (IOException e) {
                        System.out.println("Error writing to file.");;
                    }
                    break;
                case "9":
                    System.out.println("Signing out...");
                    return;
            }
        }
    }

    public static String getMenuInput(int start, int end, Scanner scanner) {
        System.out.println("What would you like to do next? Please enter a number " + start + " through " + end + ":");
        while (true) {
            String option = scanner.nextLine();
            try {
                int input = Integer.parseInt(option);
                if (input >= start && input <= end) {
                    return option;
                } else System.out.println("Invalid input. Please enter a number from " + start + " to " + end + ":");
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number:");
            }
        }
    }

    public static boolean getStoreMenuInput(Scanner scanner, Seller user, ObjectMapper objectMapper) throws IOException {
        String option = getMenuInput(1, 4, scanner);
        switch (option) {
            case "1":
                System.out.println("What would you like the name of the store to be?");
                String name = scanner.nextLine();
                if (!user.getStores().containsKey(name)) user.createNewStore(name, objectMapper);
                else System.out.println("Sorry, you already have a store with this name.");
                break;
            case "2":
                System.out.println("What is the name of the store you would like to edit?");
                String storeName = scanner.nextLine();
                if (user.getStores().containsKey(storeName)) {
                    System.out.println("What will the new name of your store be?");
                    String newStoreName = scanner.nextLine();
                    user.editStore(storeName, newStoreName, objectMapper);
                }
                else System.out.println("Sorry, we can't find a store with name " + storeName);
                break;
            case "3":
                System.out.println("What store would you like to delete");
                String deletedStoreName = scanner.nextLine();
                if (user.getStores().containsKey(deletedStoreName)){
                    String dir = "/sellers/" + user.getUsername() + "/stores";
                    JsonUtils.removeObjectFromJson(dir, deletedStoreName, objectMapper);
                }
                else System.out.println("Sorry, we can't find a store with name " + deletedStoreName);
                break;
            case "4":
                return false;
        }
        return true;
    }

    public static boolean getItemMenuInput(Scanner scanner, Seller user, ObjectMapper objectMapper) throws IOException {
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
                    }
                    else System.out.println("Sorry, we can't find a store with this name.");
                }
                break;
            case "2":
                while (true) {
                    System.out.println("What is the name of the item you would like to restock?");
                    String itemName = scanner.nextLine();
                    Item itemToChange = getItemFromAllStores(itemName, user);
                    if (itemToChange != null){
                        Store store = user.getStoreByItem(itemToChange);
                        int stock = (int) getValidDouble(scanner, "What would you like the item stock to be?");
                        itemToChange.setStock(stock);
                        String dir = "/sellers/" + user.getUsername() + "/stores/" + store.getName() + "/stockItems";
                        JsonUtils.addObjectToJson(dir, itemName, itemToChange, objectMapper);
                        break;
                    }
                    else System.out.println("Sorry, we can't find an item with this name.");
                }
                break;
            case "3":
                System.out.println("What item would you like to delete");
                String deletedItemName = scanner.nextLine();
                Item itemToDelete = getItemFromAllStores(deletedItemName, user);
                if (itemToDelete != null){
                    user.getStoreByItem(itemToDelete).deleteItem(user.getUsername(), deletedItemName, objectMapper);
                }
                else System.out.println("Sorry, we can't find an item with name " + deletedItemName);
                break;
            case "4":
                return false;
        }
        return true;
    }

    public static Item getItemFromAllStores(String itemName, Seller user) {
        ArrayList<Item> items = user.getAllStoreItems("stock");
        for (Item item : items) {
            if (item.getName().equals(itemName)) {
                return item;
            }
        }
        return null;

    }
    public static double getValidDouble(Scanner scanner, String message) {
        while(true) {
            System.out.println(message);
            try {
                double input = scanner.nextDouble();
                scanner.nextLine();
                if (input <= 0) {
                    System.out.println("Please enter a number greater than 0.");
                }
                else return input;
            } catch (Exception e) {
                System.out.println("Please enter a valid number.");
            }
        }

    }
    public static Item createItem(Scanner scanner, Seller user) {

        String name;
        while (true) {
            System.out.println("Please enter a name for the product:");
            name = scanner.nextLine();
            boolean doesExist = getItemFromAllStores(name, user) != null;
            if (!doesExist) break;
            else System.out.println("Sorry, this item already exists. Try another name.");
        }
        System.out.println("Please enter a description for the product:");
        String description = scanner.nextLine();
        int stock = (int) getValidDouble(scanner, "Please enter how many of these items you would like to list:");
        double price = getValidDouble(scanner, "Please enter the price of the item:");
        HashMap<String, Integer> sellerHashmap = new HashMap<String, Integer>();
        sellerHashmap.put(user.getUsername(), stock);
        return new Item(name, description, stock, -1, price, null, sellerHashmap);
    }


    public static void printSellerItemMenu() {
        System.out.println("What would you like to do?");
        System.out.println("\t(1) List new item ~");
        System.out.println("\t(2) Restock items ~");
        System.out.println("\t(3) Delete items ~");
        System.out.println("\t(4) Back");
    }
    public static void printSellerStoreMenu() {
        System.out.println("What would you like to do?");
        System.out.println("\t(1) Create a store ~");
        System.out.println("\t(2) Edit a store ~");
        System.out.println("\t(3) Delete a store ~");
        System.out.println("\t(4) Back");
    }
    public static void printSellerMenu() {
        System.out.println("What would you like to do?");
        System.out.println("\t(1) List, edit, or delete items ~");
        System.out.println("\t(2) Create, edit, or delete stores ~");
        System.out.println("\t(3) View all listed products ~");
        System.out.println("\t(4) View all sold products ~");
        System.out.println("\t(5) View all product buyers ~");
        System.out.println("\t(6) Edit account");
        System.out.println("\t(7) Delete account");
        System.out.println("\t(8) Export store items to CSV ~");
        System.out.println("\t(9) Sign-out ~");
    }
}

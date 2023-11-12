import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
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

            //

            //START OF USER FLOW
            Person user = enterCredentials(scanner, objectMapper);
            if (user instanceof Buyer){
                startBuyerFlow((Buyer) user, marketplace, scanner, objectMapper);

            } else if (user instanceof Seller) {
                startSellerFlow((Seller) user, scanner, objectMapper);
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
        while (true) {
            System.out.println("Welcome " + user.getFirstName() + " " + user.getLastName() + "!");
            System.out.println("(1) View store information");
            System.out.println("(2) View marketplace");
            System.out.println("(3) View cart");
            System.out.println("(4) Signout");

            String input = scanner.nextLine();
            while (!(input.equals("1") || input.equals("2"))) {
                System.out.println("Invalid input");
                input = scanner.nextLine();
            }
            if (input.equals("1")) {
                marketplace.viewStoreInfo(scanner);
            } else if (input.equals("2")) {
                marketplace.buyerFlow(scanner, (Buyer) user, objectMapper);
            } else if (input.equals("3")) {
                marketplace.cartFlow(scanner, (Buyer) user, objectMapper);
            } else if (input.equals("4")) {
                break;
            }
        }
    }
    public static void startSellerFlow(Seller user, Scanner scanner, ObjectMapper objectMapper) {
        System.out.println("Welcome " + user.getFirstName() + " " + user.getLastName() + "!");
        while (true) {
            printSellerMenu();
            String option = getMenuInput(1, 5, scanner);
            switch (option) {
                case "1":
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
                    break;
                case "3":
                    System.out.println("All listed products");
                    user.getAllStockItems("stock");
                    break;
                case "4":
                    System.out.println("All sold products");
                    user.getAllStockItems("sold");
                    break;
                case "5":
                    System.out.println("Signing out...");
                    return;
            }
        }
    }

    public static String getMenuInput(int start, int end, Scanner scanner) {
        System.out.println("What would you like to do next? Please enter a number " + start + " through " + end + ":");
        String option = scanner.nextLine();
        while (true) {
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
    public static void printSellerItemMenu() {

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
        System.out.println("\t(5) Signout");
    }
}

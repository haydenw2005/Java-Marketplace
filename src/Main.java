import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


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
                //startBuyerFlow()

                ArrayList<Item> itemsList = marketplace.getAllMarketPlaceItems();
                marketplace.listProducts(itemsList);
                System.out.println("Search for a product: enter 'search'");
                System.out.println("Sort products by price: enter 'sort price'");
                System.out.println("Sort products by quantity in stock: enter 'sort quantity'");

                String input = scanner.nextLine();

                if (input.equals("search")) {
                    marketplace.listProducts(marketplace.searchProducts(scanner, itemsList));
                    input = scanner.nextLine();
                } else if (input.equals("sort price")) {
                    marketplace.listProducts(marketplace.sortByPrice(itemsList));
                    input = scanner.nextLine();
                } else if (input.equals("sort quantity")) {
                    marketplace.listProducts(marketplace.sortByQuantity(itemsList));
                    input = scanner.nextLine();
                }

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

    public static void startSellerFlow(Seller user, Scanner scanner, ObjectMapper objectMapper) {
        System.out.println("Welcome " + user.getFirstName() + " " + user.getLastName() + "!");
        printSellerMenu();
        String option = getMenuInput(1, 5, scanner);
        switch (option) {
            case "1":
                break;
            case "2":
                printSellerStoreMenu();
                getStoreMenuInput(scanner, user, objectMapper);
                break;
            case "3":
                break;
            case "4":
                break;
            case "5":
                break;
        }
    }

    public static String getMenuInput(int start, int end, Scanner scanner) {
        System.out.println("Please enter a number " + start + " through " + end + ":");
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

    public static void getStoreMenuInput(Scanner scanner, Seller user, ObjectMapper objectMapper) {
        String option = getMenuInput(1, 4, scanner);
        switch (option) {
            case "1":
                System.out.println("What would you like the name of the store to be?");
                String name = scanner.nextLine();
                if (!user.getStores().containsKey(name)) user.createNewStore(name, objectMapper);
                else System.out.println("Sorry, you already have a store with this name.");


            case "2":

            case "3":

            case "4":

        }
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
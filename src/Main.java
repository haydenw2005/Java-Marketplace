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

            //START OF USER FLOW
            String sessionUsername = enterCredentials(scanner, objectMapper);
            //by tracking sessionUsername we know who is using the system
            System.out.println(sessionUsername);
            //CONTINUE USER FLOW HERE

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String enterCredentials(Scanner scanner, ObjectMapper objectMapper) throws IOException {
        Marketplace marketplace = JsonUtils.getObject(objectMapper, Marketplace.class);
        System.out.println("Welcome. Would you like to sign in (1) or sign up (2)?");
        String response = scanner.nextLine();
        String sessionUsername = null;
        while (!(response.equals("1") || response.equals("2"))) {
            System.out.println("Invalid. Please enter 1 (buyer) or 2 (seller).");
            response = scanner.nextLine();
        }
        if (response.equals("1")) {
            sessionUsername = marketplace.signIn(scanner);
        } else {
            sessionUsername = marketplace.signUp(scanner, objectMapper);
        }
        return sessionUsername;
    }



}



//Good code, lets turn this into functions, probably in Marketplace class

/* User can search for product by name or description:
ArrayList<Item> itemsList = marketplace.getAllMarketPlaceItems();
System.out.println("Search for a product: ");
String search = scanner.nextLine();
search = search.toUpperCase();

for (int i = 0; i < itemsList.size(); i++) {
    if (itemsList.get(i).getName().toUpperCase().contains(search) ||
            itemsList.get(i).getDescription().toUpperCase().contains(search)) {
        System.out.println(itemsList.get(i).getName());
    }
}
*/

/* Sort products by price:
for (int i = 0; i < itemsList.size(); i++) {
    for (int j = i; j < itemsList.size() - 1; j++) {
        if (itemsList.get(i).getPrice() < itemsList.get(j + 1).getPrice()) {
            Item temp = itemsList.get(i);
            itemsList.set(i, itemsList.get(j + 1));
            itemsList.set(j + 1, temp);
        }
    }
    System.out.println(i + 1 + ". " + itemsList.get(i).getName());
}
*/

/* Sort products by quantity in stock:
for (int i = 0; i < itemsList.size(); i++) {
    for (int j = i; j < itemsList.size() - 1; j++) {
        if (itemsList.get(i).getCount() < itemsList.get(j + 1).getCount()) {
            Item temp = itemsList.get(i);
            itemsList.set(i, itemsList.get(j + 1));
            itemsList.set(j + 1, temp);
        }
    }
    System.out.println(i + 1 + ". " + itemsList.get(i).getName());
}
*/
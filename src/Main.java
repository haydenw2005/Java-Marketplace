import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        File jsonFile = new File("data.json");

        if (!jsonFile.exists()) {
            System.out.println("JSON file does not exist.");
            return;
        }

        try (FileReader fileReader = new FileReader(jsonFile)) {
            ObjectMapper objectMapper = new ObjectMapper();
            Marketplace marketplace = objectMapper.readValue(fileReader, Marketplace.class);
            ArrayList<Item> itemsList = marketplace.getAllMarketPlaceItems(); //RYAN USE THIS, (PRINTS OUT ALL ITEM NAMES )

            /* User can search for product by name or description:
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Sign in or sign up..."); //START FLOW HERE

        // USER STARTS BY SIGNING IN

        //CHECK JSON FOR USER

        //IF USER IS A SELLER, INSTANTIATE SELLER CLASS WITH SELLER OBJECT FROM JSON

        //ELSE IF USER IS A BUYER, INSTANTIATE BUYER CLASS WITH BUYER OBJECT FROM JSON

        // Test

    }

}
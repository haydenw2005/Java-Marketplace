import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        File jsonFile = new File("data.json");

        if (!jsonFile.exists()) {
            System.out.println("JSON file does not exist.");
            return;
        }

        try (FileReader fileReader = new FileReader(jsonFile)) {
            ObjectMapper objectMapper = new ObjectMapper();
            Marketplace marketplace = objectMapper.readValue(fileReader, Marketplace.class);
            ArrayList<Item> itemsList = marketplace.getAllMarketPlaceItems(); //RYAN USE THIS, (PRINTS OUT ALL ITEM NAMES )

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
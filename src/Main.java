import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

            // Access Sellers and Buyers
            Map<String, Seller> sellers = marketplace.getSellers();
            Map<String, Buyer> buyers = marketplace.getBuyers();

            for (Map.Entry<String, Seller> sellerEntry : sellers.entrySet()) {
                String sellerUsername = sellerEntry.getKey();
                Seller seller = sellerEntry.getValue();
                System.out.println(sellerUsername);

            }

            for (Map.Entry<String, Buyer> buyerEntry : buyers.entrySet()) {
                String buyerUsername = buyerEntry.getKey();
                Buyer buyer = buyerEntry.getValue();
                System.out.println(buyerUsername);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Sign in or sign up..."); //START FLOW HERE

        // USER STARTS BY SIGNING IN

        //CHECK JSON FOR USER

        //IF USER IS A SELLER, INSTANTIATE SELLER CLASS WITH SELLER OBJECT FROM JSON

        //ELSE IF USER IS A BUYER, INSTANTIATE BUYER CLASS WITH BUYER OBJECT FROM JSON

    }
}
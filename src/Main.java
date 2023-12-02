import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Scanner;
import javax.swing.*;

/**
 * Main class representing the entry point for the marketplace application.
 *
 * <p>
 * Purdue University -- CS18000 -- Fall 2023
 * </p>
 *
 * @author Hayden, Soham, and Ryan
 * @version November 13, 2023
 */
public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Marketplace marketplace = JsonUtils.objectByKey(objectMapper, "", Marketplace.class);
            Person user = marketplace.enterCredentials(objectMapper);
            SwingUtilities.invokeLater(new MarketplaceGUI(marketplace, user, objectMapper));

            /*
            if (user instanceof Buyer) {
                marketplace.startBuyerFlow((Buyer) user, scanner, objectMapper);

            } else if (user instanceof Seller) {
                marketplace.startSellerFlow((Seller) user, scanner, objectMapper);
            }

             */


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

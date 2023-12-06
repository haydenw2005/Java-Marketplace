import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
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

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Socket socket = new Socket("localhost", 4242);
            
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            
            Marketplace marketplace = (Marketplace) ois.readObject();
            Person user = marketplace.enterCredentials(objectMapper);
            oos.writeObject(user);
            oos.flush();
            SwingUtilities.invokeLater(new MarketplaceGUI(marketplace, user, objectMapper, ois, oos));

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

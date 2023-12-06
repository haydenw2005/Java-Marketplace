import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Server {
    public static void main(String[] args) {
        
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            ServerSocket serverSocket = new ServerSocket(4242);
            
            System.out.println("Waiting for the client to connect...");
            Socket socket = serverSocket.accept();
            System.out.println("Client connected!");
            
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            // send marketplace to client
            Marketplace marketplace = JsonUtils.objectByKey(objectMapper, "", Marketplace.class);
            oos.writeObject(marketplace);
            oos.flush();
            System.out.println("Marketplace sent to client!");
            
            Person user = (Person) ois.readObject(); // receive person object from client
            Server server = new Server();

            while (true) {
                String command = (String) ois.readObject();
                System.out.println("Client Requested " + command);
                if (command.equals("buyItem")) {
                    Item selectedItem = (Item) ois.readObject();  // get parameters required from buyitem from user
                    String numItems = (String) ois.readObject();  // get parameters required from buyitem from user
                    ((Buyer) user).buyItem(selectedItem, marketplace, objectMapper, numItems);
                    marketplace = server.getUpdatedMarketPlace(objectMapper);
                    oos.writeObject(marketplace);// update client with new marketplace object
                    oos.flush();
                }
                if (command.equals("updateMarketplace")) {
                    marketplace = server.getUpdatedMarketPlace(objectMapper);
                    oos.writeObject(marketplace);
                    oos.flush();
                }
            }

        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public Marketplace getUpdatedMarketPlace(ObjectMapper objectMapper) throws IOException {
        return JsonUtils.objectByKey(objectMapper, "", Marketplace.class);
    }
}

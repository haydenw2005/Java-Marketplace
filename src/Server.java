import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Server {
    public static void main(String[] args) {
        int portNumber = 4242;


        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            System.out.println("Listeing on port: "  + portNumber);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Accepted connection from " + socket.getInetAddress());
                ClientThread clientThread = new ClientThread(socket);
                clientThread.start();

            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public Marketplace getUpdatedMarketPlace(ObjectMapper objectMapper) throws IOException {
        return JsonUtils.objectByKey(objectMapper, "", Marketplace.class);
    }
    public Buyer getUpdatedBuyer(String username, ObjectMapper objectMapper) throws IOException {
        return JsonUtils.objectByKey(objectMapper, "/buyers/" + username, Buyer.class);
    }

    private static class ClientThread extends Thread {
        private final Socket clientSocket;

        public ClientThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                oos.flush();
                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());

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
                    if (command.equals("updateBuyer")) {
                        user = server.getUpdatedBuyer(user.getUsername(), objectMapper);
                        oos.writeObject((Buyer) user);
                        oos.flush();
                    }
                    if (command.equals("addToCart")) {
                        Item selectedItem = (Item) ois.readObject();  // get parameters required from addtocart from user
                        ((Buyer) user).addItemToCart(selectedItem, objectMapper);
                        marketplace = server.getUpdatedMarketPlace(objectMapper);
                        oos.writeObject(marketplace);
                        oos.flush();
                    }
                    if (command.equals("buyCart")) {
                        ((Buyer) user).buyCart(marketplace, objectMapper);
                        marketplace = server.getUpdatedMarketPlace(objectMapper);
                        user = server.getUpdatedBuyer(user.getUsername(), objectMapper);
                        oos.writeObject(marketplace);
                        oos.writeObject((Buyer) user);
                        oos.flush();
                    }
                    if (command.equals("editUser")) {
                        Object[] editUserObjects = (Object[]) ois.readObject();
                        user.setPassword((String) editUserObjects[0]);
                        user.setFirstName((String) editUserObjects[1]);
                        user.setLastName((String) editUserObjects[2]);
                        user.setEmail((String) editUserObjects[3]);
                        if (user instanceof Buyer) {
                            marketplace.addBuyerAccount(user.getUsername(), (Buyer) user, objectMapper);
                        } else {
                            marketplace.addSellerAccount(user.getUsername(), (Seller) user, objectMapper);
                        }
                        user = server.getUpdatedBuyer(user.getUsername(), objectMapper);
                        oos.writeObject(user);
                        oos.flush();
                    }
                    if (command.equals("deleteUser")) {
                        if (user instanceof Buyer) {
                            String dir = "/buyers";
                            JsonUtils.removeObjectFromJson(dir, user.getUsername(), objectMapper);
                        } else {
                            String dir = "/sellers";
                            JsonUtils.removeObjectFromJson(dir, user.getUsername(), objectMapper);
                        }
                    }
                }
            } catch (ClassNotFoundException e){
                e.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}


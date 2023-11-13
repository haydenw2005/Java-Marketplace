import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CsvUtils {
    public static void writePurchaseHistoryToCSV(String filename, Buyer buyer) throws IOException {
        ArrayList<Item> purchasedItems = buyer.getPurchasedItems();
        if (!(purchasedItems.isEmpty())) {
            File csvOutputFile = new File(filename + ".csv");
            if (!csvOutputFile.createNewFile()) {
                System.out.println("File already exists.");
                return;
            }
            PrintWriter writer = new PrintWriter(new FileOutputStream(csvOutputFile, false));
            writer.println("Product, Description, Price, Stock");
            for (int i = 0; i < purchasedItems.size(); i++) {
                writer.println(purchasedItems.get(i).getName() + ", "
                        + purchasedItems.get(i).getDescription()  + ", " + purchasedItems.get(i).getPrice() 
                        + ", " + purchasedItems.get(i).getCount());
            }

            writer.close();
        } else {
            System.out.println("No purchased items found.");
        }
    }

    public static void writeProductsToCSV(String filename, Seller seller) throws IOException {
        File csvOutputFile = new File(filename + ".csv");
        if (!csvOutputFile.createNewFile()) {
            System.out.println("File already exists.");
            return;
        }
        PrintWriter writer = new PrintWriter(new FileOutputStream(csvOutputFile, false));

        ArrayList<Store> stores = new ArrayList<Store>();
        for (Map.Entry<String, Store> storeEntry : seller.getStores().entrySet()) {
            stores.add(storeEntry.getValue());
        }

        writer.println("Product, Description, Price, Stock, Store");

        for (int i = 0; i < stores.size(); i++) {
            ArrayList<Item> items = new ArrayList<Item>();
            for (Map.Entry<String, Item> itemEntry : stores.get(i).getStockItems().entrySet()) {
                items.add(itemEntry.getValue());
            }
            
            for (int j = 0; j < items.size(); j++) {
                writer.println(items.get(j).getName() + ", "
                        + items.get(j).getDescription()  + ", " + items.get(j).getPrice() 
                        + ", " + items.get(j).getStock() + ", " + stores.get(i).getName());
            }
        }

        writer.close();
    }

    public static void importFromCSV(String filename, Seller seller, ObjectMapper objectMapper) throws FileNotFoundException {
        File csvOutputFile = new File(filename + ".csv");
        if (!csvOutputFile.exists()) {
            System.out.println("File does not exist.");
            return;
        }
        BufferedReader buffer = new BufferedReader(new FileReader(csvOutputFile));
        ArrayList<String> lines = new ArrayList<String>();
        String line;
        
        try {
            buffer.readLine(); // ignore first line, column names
            while ((line = buffer.readLine()) != null) {
                if (line.isEmpty()) {
                    break;
                }
                lines.add(line);
            }
            for (int i = 0; i < lines.size(); i++) {
                String[] data = lines.get(i).split(", ");

                // 0 - Name, 1 - Description, 2 - Price, 3 - Stock, 4 - Store
                Store currentStore = new Store(data[4], new HashMap<String, Item>(), new HashMap<String, Item>());
                Map<String, Integer> sellerObject = new HashMap<String, Integer>();
                sellerObject.put(seller.getUsername(), Integer.parseInt(data[3]));
                Map<String, Item> stockItem = new HashMap<String, Item>();
                stockItem.put(data[0], new Item(data[0], data[1], Integer.parseInt(data[3]), -1, Double.parseDouble(data[2]), 
                    null, sellerObject));
                currentStore.setStockItems(stockItem);
                String dir = "/sellers/" + seller.getUsername() + "/stores";
                if (JsonUtils.hasKey(dir, currentStore.getName(), objectMapper)) {
                    String stockDir = dir + "/" + currentStore.getName() + "/stockItems";
                    JsonUtils.addObjectToJson(stockDir, data[0], stockItem.get(data[0]), objectMapper);
                } else {
                    JsonUtils.addObjectToJson(dir, currentStore.getName(), currentStore, objectMapper);
                }
                Map<String, Store> updatedStores = seller.getStores();
                updatedStores.put(currentStore.getName(), currentStore);
                seller.setStores(updatedStores);

            }
            buffer.close();
        } catch (IOException e) {
            System.out.println("An error occured while importing.");
            return;
        }
        
    }
}
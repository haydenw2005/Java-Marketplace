import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

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
}
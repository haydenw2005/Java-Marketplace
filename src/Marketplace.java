import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Map;
public class Marketplace {
    @JsonProperty("Buyers")
    private Map<String, Buyer> buyers;
    @JsonProperty("Sellers")
    private Map<String, Seller> sellers;

    public Map<String, Buyer> getBuyers() {
        return buyers;
    }
    public Map<String, Seller> getSellers() {
        return sellers;
    }

    public ArrayList<Item> getAllMarketPlaceItems() {
        ArrayList<Item> itemsArray = new ArrayList<>();;
        for (Map.Entry<String, Seller> sellerEntry : sellers.entrySet()) {
            Seller seller = sellerEntry.getValue();
            Map<String, Store> stores = seller.getStores();
            for (Map.Entry<String, Store> storeEntry : stores.entrySet()) {
                Store store = storeEntry.getValue();
                Map<String, Item> items = store.getStockItems();
                for (Map.Entry<String, Item> itemEntry : items.entrySet()) {
                    itemsArray.add(itemEntry.getValue());
                    System.out.println(itemEntry.getValue().getName());
                }
            }
        }
        return itemsArray;
    }

}

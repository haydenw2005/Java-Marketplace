import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class Store {
    private String name;
    private Map<String, Item> stockItems;
    private Map<String, Item> soldItems;

    public Store() {}
    public Store(@JsonProperty("name") String name, @JsonProperty("stockItems") Map<String, Item> stockItems,
                 @JsonProperty("soldItems") Map<String, Item> soldItems) {
        this.name = name;
        this.stockItems = stockItems;
        this.soldItems = soldItems;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Map<String, Item> getStockItems() {
        return stockItems;
    }
    public void setStockItems(Map<String, Item> stockItems) {
        this.stockItems = stockItems;
    }
    public Map<String, Item> getSoldItems() {
        return soldItems;
    }
    public void setSoldItems(Map<String, Item> soldItems) {
        this.soldItems = soldItems;
    }

    public void addToStockItems(Item item, String userName, ObjectMapper objectMapper) {
        try {
            String dir = "/sellers/" + userName + "/stores/" + this.getName() + "/stockItems";
            JsonUtils.addObjectToJson(dir, item.getName(), item, objectMapper);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error creating item.");
        }
    }



    public void deleteItem(String username, String deletedItemName, ObjectMapper objectMapper) {
        try {
            String dir = "/sellers/" + username + "/stores/" + this.getName() + "/stockItems";
            JsonUtils.removeObjectFromJson(dir, deletedItemName, objectMapper);
        } catch (Exception e) {
            System.out.println("Error deleting item");
        }
    }


    public int getNumProductsSold() {
        int count = 0;
        for (Map.Entry<String, Item> itemEntry : soldItems.entrySet()) {
            count += itemEntry.getValue().getStock();
        }

        return count;
    }

    public ArrayList<Item> getProductsPurchasedFromStore(Marketplace marketplace, Buyer buyer) {
        ArrayList<Item> productsPurchased = new ArrayList<Item>();
        ArrayList<Item> purchasedItems = buyer.getPurchasedItems();
        for (int i = 0; i < purchasedItems.size(); i++) {
            if (marketplace.getStore(purchasedItems.get(i)).getName().equals(name)) {
                productsPurchased.add(purchasedItems.get(i));
            }
        }
        return productsPurchased;
    }

    public int getTotalItemsPurchased(Marketplace marketplace, Buyer buyer) {
        int count = 0;
        ArrayList<Item> list = this.getProductsPurchasedFromStore(marketplace, buyer);
        for (int i = 0; i < list.size(); i++) {
            count += list.get(i).getCount();
        }

        return count;
    }
}

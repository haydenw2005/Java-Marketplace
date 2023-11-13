import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a Seller in the marketplace, extending the Person class.
 *
 * <p>Purdue University -- CS18000 -- Fall 2023</p>
 *
 * @author Hayden, Vishal
 * @version November 13, 2023
 */
public class Seller extends Person {

    private Map<String, Store> stores;
    public Seller() {}
    public Seller(@JsonProperty("username") String username, @JsonProperty("password") String password,
                  @JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName,
                  @JsonProperty("email") String email,
                  @JsonProperty("stores") Map<String, Store> stores) {
        super(username, password, firstName, lastName, email);
        this.stores = stores;
    }

    public void createNewStore(String name, ObjectMapper objectMapper) {
       try {
           Store newStore = new Store(name, new HashMap<>(), new HashMap<>());
           String dir = "/sellers/" + this.getUsername() + "/stores";
           JsonUtils.addObjectToJson(dir, name, newStore, objectMapper);
       } catch (Exception e) {
           e.printStackTrace();
           System.out.println("Error creating store.");
       }
    }

    public void editStore(String name, String newName, ObjectMapper objectMapper) {
        try {
            String dir = "/sellers/" + this.getUsername() + "/stores";
            Store store = JsonUtils.objectByKey(objectMapper, dir + "/" + name, Store.class);
            JsonUtils.removeObjectFromJson(dir, name, objectMapper);
            store.setName(newName);
            JsonUtils.addObjectToJson(dir, newName, store, objectMapper);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error editing store.");
        }
    }

    public ArrayList<Item> getAllStoreItems(String type) {
        ArrayList<Item> storeItems = new ArrayList<>();
        for (Map.Entry<String, Store> storeEntry : stores.entrySet()) {
            Store store = storeEntry.getValue();
            Map<String, Item> items;
            if (type.equals("stock")) items = store.getStockItems();
            else if (type.equals("sold")) items = store.getSoldItems();
            else return null;
            for (Map.Entry<String, Item> itemEntry : items.entrySet()) {
                storeItems.add(itemEntry.getValue());
            }
        }
        return storeItems;
    }

    public HashMap<String, Integer> allBuyers()  {
        HashMap<String, Integer> personCount = new HashMap<>();
        for (Map.Entry<String, Store> storeEntry : stores.entrySet()) {
            Store store = storeEntry.getValue();
            Map<String, Item> items = store.getSoldItems();
            for (Map.Entry<String, Item> itemEntry : items.entrySet()) {
                Item item = itemEntry.getValue();
                Map<String, Integer> peopleMap = item.getBuyersObject();
                for (Map.Entry<String, Integer> peopleEntry : peopleMap.entrySet()) {
                    String key = peopleEntry.getKey();
                    if (!personCount.containsKey(key)) {
                        personCount.put(key, peopleEntry.getValue());
                    } else {
                        personCount.put(key, personCount.get(key) + peopleEntry.getValue());
                    }
                }
            }
        }
        return personCount;
    }

    public Store getStoreByItem(Item item) {
        for (Map.Entry<String, Store> storeEntry : stores.entrySet()) {
            Store store = storeEntry.getValue();
            Map<String, Item> items = store.getStockItems();
            for (Map.Entry<String, Item> itemEntry : items.entrySet()) {
                if (itemEntry.getValue().getName().equals(item.getName())) {
                    return store;
                }
            }
        }
        return null;
    }

    @Override
    public String getFirstName() {
        return super.getFirstName();
    }

    @Override
    public String getLastName() {
        return super.getLastName();
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    public Map<String, Store> getStores() {
        return stores;
    }

    public Store getStoreByName(String name) {
        return stores.get(name);
    }


}
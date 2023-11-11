import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Buyer extends Person {

    private Map<String, Item> cart;
    private Map<String, Item> purchaseHistory;

    public Buyer() {}
    public Buyer(@JsonProperty("username") String username, @JsonProperty("password") String password,
                 @JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName,
                 @JsonProperty("cart") Map<String, Item> cart,
                 @JsonProperty("purchaseHistory") Map<String, Item> purchaseHistory) {
        super(username, password, firstName, lastName);
        this.cart = cart;
        this.purchaseHistory = purchaseHistory;
    }
    
    // --- getters and setters ---
    public Map<String, Item> getCart() {
        return cart;
    }
    public void setCart(Map<String, Item> cart) {
        this.cart = cart;
    }
    public Map<String, Item> getPurchaseHistory() {
        return purchaseHistory;
    }
    public void setPurchaseHistory(Map<String, Item> purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }
    
    @Override
    public String getFirstName() {
        return super.getFirstName();
    }
    
    @Override
    public String getLastName() {
        return super.getLastName();
    }
    // --- getters and setters ---

    // other functions
    public void addItemToCart(Item item, ObjectMapper objectMapper) {
        if (item.getCount() < item.getStock()) {
            try {
                String dir = "/buyers/" + this.getUsername() + "/cart";
                JsonUtils.addObjectToJson(dir, item.getName(), item, objectMapper);
            } catch (IOException e) {
                System.out.println("Error adding item to cart.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Error, not enough stock.");
        }
    }

    public void buyItem(Item item, ObjectMapper objectMapper) {
        if(item.getCount() <= item.getStock()) {
            try {
                String buyerCartDir = "/buyers/" + this.getUsername() + "/cart/" + item.getName();
                item.setTotalBoughtByBuyer(this.getUsername(), item.totalBoughtByBuyer(this.getUsername()) + item.getCount());
                item.setTotalSoldBySeller(item.totalSoldBySeller() +  item.getCount());
                JsonUtils.removeObjectFromJson(buyerCartDir, item.getName(), objectMapper);
                updateStock(item);
                addToPurchaseHistory(item, objectMapper);
                String buyerHistoryDir = "/buyers/" + this.getUsername() + "/purchaseHistory";
                Marketplace marketplace = new Marketplace();
                String sellerDir = "/sellers/" + item.getSeller() + "/" + marketplace.getStore(item).getName() + "/stockItems/";
                JsonUtils.addObjectToJson(buyerHistoryDir, item.getName(), item, objectMapper);
                JsonUtils.addObjectToJson(sellerDir, item.getName(), item, objectMapper);
                if(item.getStock() == 0) {
                    JsonUtils.removeObjectFromJson(sellerDir, item.getName(), objectMapper);
                }
            } catch (IOException e) {
                System.out.println("Error buying item.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Error, not enough stock.");
        }
    }

    public void buyCart(Item[] items, ObjectMapper objectMapper) {
        for(int i = 0; i < items.length; i++) {
            buyItem(items[i], objectMapper);
        } 
    }
    
    public void addToPurchaseHistory(Item item, ObjectMapper objectMapper) {
        try {
            String dir = "/buyers/" + this.getUsername() + "/purchaseHistory";
            JsonUtils.addObjectToJson(dir, item.getName(), item, objectMapper);
        } catch (IOException e) {
            System.out.println("Error adding item to cart.");
            e.printStackTrace();
        }
    }
    public void updateStock(Item item) {
        item.setStock(item.getStock() - item.getCount());
        item.setCount(0);
    }
}
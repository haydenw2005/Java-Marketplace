import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

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

    public ArrayList<Item> getPurchasedItems() {
        ArrayList<Item> purchasedItems = new ArrayList<Item>();
        for (Map.Entry<String, Item> itemEntry : purchaseHistory.entrySet()) {
            purchasedItems.add(itemEntry.getValue());
        }


        return purchasedItems;
    }

    // --- getters and setters ---

    // other functions
    public void addItemToCart(Item item, ObjectMapper objectMapper) {
        if (item.getCount() < item.getStock()) {
            try {
                String cartDir = "/buyers/" + this.getUsername() + "/cart";
                String itemDir = cartDir + "/" + item.getName();
                if(JsonUtils.hasKey(cartDir, item.getName(), objectMapper)) {
                    // iterate item count if item already exists in cart
                    Item cartItem = JsonUtils.getObjectByKey(objectMapper, itemDir, Item.class);
                    item.setCount(cartItem.getCount() + item.getCount()); 
                } 
                JsonUtils.addObjectToJson(cartDir, item.getName(), item, objectMapper);
                System.out.println("Item added to cart successfully.");
            } catch (Exception e) {
                System.out.println("Error adding item to cart.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Error, not enough stock.");
        }
    }
    public void buyItem(Item item, Marketplace marketplace, ObjectMapper objectMapper) {
        if(item.getCount() <= item.getStock()) {
            try {
                String buyerCartDir = "/buyers/" + this.getUsername() + "/cart";
                
                // Update buyers and sellers objects
                if (item.getBuyersObject() != null && item.getBuyersObject().containsKey(this.getUsername())) {
                    item.setTotalBoughtByBuyer(this.getUsername(), item.totalBoughtByBuyer(this.getUsername()) + item.getCount());
                } else {
                    item.setBuyersObject(new HashMap<String,Integer>());
                    item.setTotalBoughtByBuyer(this.getUsername(), item.getCount());
                }
                item.setTotalSoldBySeller(item.totalSoldBySeller() +  item.getCount());
                
                if (JsonUtils.hasKey(buyerCartDir, item.getName(), objectMapper)) {
                    // Remove from cart if exists
                    JsonUtils.removeObjectFromJson(buyerCartDir, item.getName(), objectMapper);
                }
                
                // Add to sold items in seller object
                String sellerSoldDir = "/sellers/" + item.findSeller() + "/stores/" + marketplace.getStore(item).getName() + "/soldItems";
                if(marketplace.getStore(item).getSoldItems().containsKey(item.getName())) {
                    // Item already exists, update count first
                    Item cartItem = JsonUtils.getObjectByKey(objectMapper, sellerSoldDir + "/" + item.getName(), Item.class);
                    item.setCount(cartItem.getCount() + item.getCount()); 
                } 
                JsonUtils.addObjectToJson(sellerSoldDir, item.getName(), item, objectMapper);
                
                addToPurchaseHistory(item, objectMapper); 
                updateStock(item); // Changes the stock after buying
                

                // Update Stock JSON
                String sellerStockDir = "/sellers/" + item.findSeller() + "/stores/" + marketplace.getStore(item).getName() + "/stockItems";
                JsonUtils.addObjectToJson(sellerStockDir, item.getName(), item, objectMapper);
               
                if(item.getStock() <= 0) {
                    // Remove from stockItems if stock is over
                    JsonUtils.removeObjectFromJson(sellerStockDir, item.getName(), objectMapper);
                }
            } catch (Exception e) {
                System.out.println("Error buying item.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Error, not enough stock.");
        }
    }

    public void buyCart(Marketplace marketplace, ObjectMapper objectMapper) {
        Collection<Item> values = cart.values();
        Item[] items = values.toArray(new Item[0]);

        try {
            for(int i = 0; i < items.length; i++) {
                buyItem(items[i], marketplace, objectMapper);
            } 
            System.out.println("Successfully purchased all items in cart!");
        } catch (Exception e) {
            System.out.println("Error buying cart.");
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
    
    /**
     *  Prints out String of all cart items for current buyer.
     */
    public void showAllCartItems() {
        if(!(cart.isEmpty())) {
            for (Map.Entry<String, Item> cartEntry : cart.entrySet()) {
                Item item = cartEntry.getValue();
                System.out.println(item.toString());
            } 
        } else {
            System.out.println("Cart is empty! Add items to cart from the Marketplace.");
        }
        
    }

}
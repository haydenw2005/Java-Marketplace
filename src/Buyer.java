import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

/**
 * Represents a buyer in the system, extending the {@code Person} class. A buyer
 * has a cart for
 * adding items, a purchase history, and methods to interact with the
 * marketplace.
 *
 * <p>
 * Purdue University -- CS18000 -- Fall 2023
 * </p>
 *
 * @author Soham, Ryan
 * @version November 13, 2023
 */
public class Buyer extends Person {

    private Map<String, Item> cart;
    private Map<String, Item> purchaseHistory;

    public Buyer() {
    }

    public Buyer(@JsonProperty("username") String username, @JsonProperty("password") String password,
            @JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName,
            @JsonProperty("email") String email,
            @JsonProperty("cart") Map<String, Item> cart,
            @JsonProperty("purchaseHistory") Map<String, Item> purchaseHistory) {
        super(username, password, firstName, lastName, email);
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
        return this.purchaseHistory;
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
        try {
            if(item.getCount() <= 0) {
                item.setCount(1);
            }
            String cartDir = "/buyers/" + this.getUsername() + "/cart";
            String itemDir = cartDir + "/" + item.getName();
            if (JsonUtils.hasKey(cartDir, item.getName(), objectMapper)) {
                // iterate item count if item already exists in cart
                Item cartItem = JsonUtils.objectByKey(objectMapper, itemDir, Item.class);
                item.setCount(cartItem.getCount() + item.getCount());
            }
            JsonUtils.addObjectToJson(cartDir, item.getName(), item, objectMapper);
            cart.put(item.getName(), item);
            // System.out.println("Item added to cart successfully.");

            JOptionPane.showMessageDialog(null, "Item added to cart", "Success!",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error adding item to cart",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void buyItem(Item item, Marketplace marketplace, ObjectMapper objectMapper, String numItems) {
        try {
            if (Integer.parseInt(numItems) <= item.getStock()) {
                item.setCount(Integer.parseInt(numItems));

                String buyerCartDir = "/buyers/" + this.getUsername() + "/cart";

                // Update buyers and sellers objects
                if (item.getBuyersObject() != null && item.getBuyersObject().containsKey(this.getUsername())) {
                    item.setTotalBoughtByBuyer(this.getUsername(),
                            item.totalBoughtByBuyer(this.getUsername()) + item.getCount());
                } else {
                    item.setBuyersObject(new HashMap<String, Integer>());
                    item.setTotalBoughtByBuyer(this.getUsername(), item.getCount());
                }
                item.setTotalSoldBySeller(item.totalSoldBySeller() + item.getCount());

                if (JsonUtils.hasKey(buyerCartDir, item.getName(), objectMapper)) {
                    // Remove from cart if exists
                    JsonUtils.removeObjectFromJson(buyerCartDir, item.getName(), objectMapper);
                    cart.remove(item.getName());
                }

                // Add to sold items in seller object
                String sellerSoldDir = "/sellers/" + item.findSeller() + "/stores/"
                        + marketplace.getStore(item).getName() + "/soldItems";
                if (marketplace.getStore(item).getSoldItems().containsKey(item.getName())) {
                    // Item already exists, update count first
                    Item cartItem = JsonUtils.objectByKey(objectMapper, sellerSoldDir + "/" + item.getName(),
                            Item.class);
                    item.setCount(cartItem.getCount() + item.getCount());
                }
                JsonUtils.addObjectToJson(sellerSoldDir, item.getName(), item, objectMapper);

                updateStock(item, Integer.parseInt(numItems)); // Changes the stock after buying
                item.setCount(item.getCount());
                addToPurchaseHistory(item, objectMapper);
                item.setCount(-1); // set count to -1 after adding to purchase history

                // Update Stock JSON
                String sellerStockDir = "/sellers/" + item.findSeller() + "/stores/"
                        + marketplace.getStore(item).getName() + "/stockItems";
                JsonUtils.addObjectToJson(sellerStockDir, item.getName(), item, objectMapper);

                if (item.getStock() <= 0) {
                    // Remove from stockItems if stock is over
                    JsonUtils.removeObjectFromJson(sellerStockDir, item.getName(), objectMapper);
                }
                

                JOptionPane.showMessageDialog(null, "Item bought", "Success!",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Not enough stock",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error buying item",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void buyCart(Marketplace marketplace, ObjectMapper objectMapper) {
        if (!(cart.isEmpty())) {
            Collection<Item> values = cart.values();
            Item[] items = values.toArray(new Item[0]);

            try {
                for (int i = 0; i < items.length; i++) {
                    buyItem(items[i], marketplace, objectMapper, "1");
                }
                JOptionPane.showMessageDialog(null, "Cart bought", "Success!",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error buying cart",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Cart is empty",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void addToPurchaseHistory(Item item, ObjectMapper objectMapper) {
        Item purchasedItem = new Item(item.getName(), item.getDescription(), -1, item.getCount(), 
                item.getPrice(), item.getBuyersObject(), item.getSellersObject());
        this.purchaseHistory.put(purchasedItem.getName(), purchasedItem);
        try {
            String dir = "/buyers/" + this.getUsername() + "/purchaseHistory";
            JsonUtils.addObjectToJson(dir, purchasedItem.getName(), purchasedItem, objectMapper);
        } catch (IOException e) {
            System.out.println("Error adding item to cart.");
            e.printStackTrace();
        }
    }

    public void updateStock(Item item, int numItems) {
        item.setStock(item.getStock() - numItems);
    }

    public ArrayList<String> cartToStringList(Marketplace marketplace) {
        ArrayList<String> cartList = new ArrayList<String>();
        if (!(cart.isEmpty())) {
            for (Map.Entry<String, Item> cartEntry : cart.entrySet()) {
                Item item = cartEntry.getValue();
                cartList.add(item.toString(marketplace));
            }
        } else {
            cartList.add("Cart is empty! Add items to cart from the Marketplace");
        }
        return cartList;
    }

    public ArrayList<String> getPurchaseHistory(Marketplace marketplace) {
        ArrayList<String> purchaseHistoryList = new ArrayList<String>();
        if (!(purchaseHistory.isEmpty())) {
            for (Map.Entry<String, Item> purchaseEntry : purchaseHistory.entrySet()) {
                Item item = purchaseEntry.getValue();
                purchaseHistoryList.add(item.toString(marketplace));
            }
        } else {
            purchaseHistoryList.add("No items purchased yet");
        }
        return purchaseHistoryList;
    }
}
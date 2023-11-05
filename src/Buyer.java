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
    // --- getters and setters ---

    // other functions
        
    public void addItemToCart(Item item) {
        cart.put(item.getName(), item);
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        try {
            writer.writeValue(new File("test.json"), cart);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buyCart(Item[] items) {
        // TODO
    }
}
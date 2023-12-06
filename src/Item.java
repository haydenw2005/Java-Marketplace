import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Map;

/**
 * Represents an item in the marketplace that can be bought and sold.
 *
 * <p>
 * Purdue University -- CS18000 -- Fall 2023
 * </p>
 *
 * @author Hayden and Soham
 * @version November 13, 2023
 */
public class Item implements Serializable {
    private String name;
    private String description;
    private int stock;
    private int count;
    private double price;
    private Map<String, Integer> buyersObject;
    private Map<String, Integer> sellersObject;

    public Item() {
    }

    /**
     * @param name
     * @param description
     * @param stock
     * @param count
     * @param price
     * @param buyersObject
     * @param sellersObject
     */
    public Item(@JsonProperty("name") String name, @JsonProperty("description") String description,
            @JsonProperty("stock") int stock, @JsonProperty("count") int count, @JsonProperty("price") double price,
            @JsonProperty("buyersObject") Map<String, Integer> buyersObject,
            @JsonProperty("sellersObject") Map<String, Integer> sellersObject) {
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.count = count;
        this.price = price;
        this.buyersObject = buyersObject;
        this.sellersObject = sellersObject;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public int totalBoughtByBuyer(String username) {
        return buyersObject.get(username);
    }

    public int totalSoldBySeller() {
        return sellersObject.get(this.findSeller());
    }

    public String findSeller() {
        return sellersObject.entrySet().iterator().next().getKey();
    }

    public void setTotalBoughtByBuyer(String username, int quantity) {
        // changes quantity bought by a buyer in buyers object
        buyersObject.put(username, quantity);
    }

    public void setTotalSoldBySeller(int quantity) {
        // changes quantity sold by a seller in sellers object
        sellersObject.put(this.findSeller(), quantity);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Map<String, Integer> getBuyersObject() {
        return buyersObject;
    }

    public void setBuyersObject(Map<String, Integer> buyersObject) {
        this.buyersObject = buyersObject;
    }

    public Map<String, Integer> getSellersObject() {
        return sellersObject;
    }

    public void setSellersObject(Map<String, Integer> sellersObject) {
        this.sellersObject = sellersObject;
    }

    public String toString(Marketplace marketplace) {
        if (stock >= 0) {
            return "Product: " + this.getName() + " | Store: " + marketplace.getStore(this).getName() +
                    " | Price: " + this.getPrice() + " | Stock: " + this.getStock();
        } else if (count >= 0) {
            return "Product: " + this.getName() + " | Store: " + marketplace.getStore(this).getName() +
                    " | Price: " + this.getPrice() + " | Count: " + this.getCount();
        }
        return null;
    }

    public boolean equalsItem(Item item) {
        if (this.name.equals(item.getName())) {
            if (this.description.equals(item.getDescription())) {
                if (this.price == item.getPrice()) {
                    return true;
                }
            }
        }
        return false;
    }

}

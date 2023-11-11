import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Item {
    private String name;
    private String description;
    private int stock;
    private int count;
    private int price;
    private Map<String, Integer> buyersObject;
    private Map<String, Integer> sellersObject;

    public Item() {}
    public Item(@JsonProperty("name") String name, @JsonProperty("description") String description, 
                 @JsonProperty("stock") int stock, @JsonProperty("count") int count, @JsonProperty("price") int price,
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
        //change json
    }

    public int getPrice() {
        return price;
    }

    public int getTotalPrice() {
        return price*count;
    }

    public int totalBoughtByBuyer(String username) {
        return buyersObject.get(username);
    }

    public int totalSoldBySeller() {
        return sellersObject.get(this.getSeller());
    }
    public String getSeller() {
        return sellersObject.entrySet().iterator().next().getKey();
    }
    public void setTotalBoughtByBuyer(String username, int quantity) {
        // changes quantity bought by a buyer in buyers object 
        buyersObject.put(username, quantity);
    }
    public void setTotalSoldBySeller(int quantity) {
        // changes quantity sold by a seller in sellers object 
        sellersObject.put(this.getSeller(), quantity);
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
    public void setPrice(int price) {
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


}

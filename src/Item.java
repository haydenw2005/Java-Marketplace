import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Item {
    private String name;
    private String description;
    private int stock;
    private int price;
    private Map<String, Integer> buyersObject;
    private Map<String, Integer> sellersObject;

    public Item() {}
    public Item(@JsonProperty("name") String name, @JsonProperty("description") String description,
                 @JsonProperty("stock") int stock, @JsonProperty("price") int price,
                 @JsonProperty("buyersObject") Map<String, Integer> buyersObject,
                 @JsonProperty("sellersObject") Map<String, Integer> sellersObject) {
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.price = price;
        this.buyersObject = buyersObject;
        this.sellersObject = sellersObject;
    }
}

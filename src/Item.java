import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Item {
    private String name;
    private String description;
    private int count;
    private int price;
    private Map<String, Integer> buyersObject;
    private Map<String, Integer> sellersObject;

    public Item() {}
    public Item(@JsonProperty("name") String name, @JsonProperty("description") String description,
                 @JsonProperty("count") int count, @JsonProperty("price") int price,
                 @JsonProperty("buyersObject") Map<String, Integer> buyersObject,
                 @JsonProperty("sellersObject") Map<String, Integer> sellersObject) {
        this.name = name;
        this.description = description;
        this.count = count;
        this.price = price;
        this.buyersObject = buyersObject;
        this.sellersObject = sellersObject;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getCount() {
        return count;
    }
}

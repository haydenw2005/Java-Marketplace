import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

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
}

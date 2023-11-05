import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
public class Marketplace {
    @JsonProperty("Buyers")
    private Map<String, Buyer> buyers;
    @JsonProperty("Sellers")
    private Map<String, Seller> sellers;

    public Map<String, Buyer> getBuyers() {
        return buyers;
    }
    public Map<String, Seller> getSellers() {
        return sellers;
    }
}

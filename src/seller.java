import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Seller extends Person {

    private Map<String, Store> stores;
    public Seller() {}
    public Seller(@JsonProperty("username") String username, @JsonProperty("password") String password,
                  @JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName,
                  @JsonProperty("stores") Map<String, Store> stores) {
        super(username, password, firstName, lastName);
        this.stores = stores;
    }
    
}

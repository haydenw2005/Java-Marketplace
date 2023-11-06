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

    @Override
    public String getFirstName() {
        return super.getFirstName();
    }

    @Override
    public String getLastName() {
        return super.getLastName();
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    public Map<String, Store> getStores() {
        return stores;
    }

    public Store getStoreByName(String name) {
        return stores.get(name);
    }
    public String[] getListOfStoresNames() {
        return stores.keySet().toArray(new String[0]);
    }
}
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a person in the system with basic information such as username, password, first name,
 * last name, and email.
 *
 * <p>Purdue University -- CS18000 -- Fall 2023</p>
 *
 * @author Soham, Hayden
 * @version November 13, 2023
 */
public class Person {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;

    public Person() {}

    public Person(@JsonProperty("username") String username, @JsonProperty("password") String password, @JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName, @JsonProperty("email") String email) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

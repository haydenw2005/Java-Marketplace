import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PersonTest {

    private Person person;

    @Before
    public void setUp() {
        person = new Person("testUser", "password", "John", "Doe", "john.doe@example.com");
    }

    @Test
    public void gettersAndSettersTest() {
        assertEquals("testUser", person.getUsername());
        assertEquals("password", person.getPassword());
        assertEquals("John", person.getFirstName());
        assertEquals("Doe", person.getLastName());
        assertEquals("john.doe@example.com", person.getEmail());

        person.setUsername("newUser");
        person.setPassword("newPassword");
        person.setFirstName("Jane");
        person.setLastName("Smith");
        person.setEmail("jane.smith@example.com");

        assertEquals("newUser", person.getUsername());
        assertEquals("newPassword", person.getPassword());
        assertEquals("Jane", person.getFirstName());
        assertEquals("Smith", person.getLastName());
        assertEquals("jane.smith@example.com", person.getEmail());
    }
}

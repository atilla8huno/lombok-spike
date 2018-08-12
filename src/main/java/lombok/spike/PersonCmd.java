package lombok.spike;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonCmd {

    private String firstName;
    private String lastName;

    public PersonCmd(Person person) {
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
    }

    public String sayMyName() {
        return "My name is " + firstName + " " + lastName;
    }
}

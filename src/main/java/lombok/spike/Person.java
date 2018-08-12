package lombok.spike;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = {"firstName"})
@ToString
public class Person {

    private String firstName;
    private String lastName;

    public String sayMyName() {
        return "My name is " + firstName + " " + lastName;
    }
}

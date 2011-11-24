package bbejeck.support.model;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 11/23/11
 * Time: 8:38 PM
 */

public class Person {

    public final String firstName;
    public final String lastName;
    public final String address;
    public final String email;


    public Person(Map<String,String> values){
        firstName = values.get("first_name");
        lastName = values.get("last_name");
        address = values.get("address");
        email = values.get("email");
    }

}

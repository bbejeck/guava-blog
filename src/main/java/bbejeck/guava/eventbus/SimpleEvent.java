package bbejeck.guava.eventbus;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 12/29/11
 * Time: 11:26 PM
 */
public class SimpleEvent {

    String name;
    String description;

    public SimpleEvent(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "SimpleEvent{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

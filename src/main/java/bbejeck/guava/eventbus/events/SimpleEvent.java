package bbejeck.guava.eventbus.events;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 1/3/12
 * Time: 10:46 PM
 */

public class SimpleEvent {

    private String name;

    public SimpleEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

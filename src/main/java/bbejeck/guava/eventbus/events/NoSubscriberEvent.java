package bbejeck.guava.eventbus.events;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 1/5/12
 * Time: 10:22 PM
 */
public class NoSubscriberEvent {

    String message = "I'm lost";

    public String getMessage() {
        return message;
    }
}

package bbejeck.guava.eventbus;

import com.google.common.eventbus.EventBus;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 12/29/11
 * Time: 11:15 PM
 */

public class EventPublisher {

    EventBus eventBus;

    public EventPublisher(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void createPurchaseEvent(String name, String amount) {
        eventBus.post(new PurchaseEvent(name, amount));
    }

    public void createSimpleEvent(String name, String description) {
        eventBus.post(new SimpleEvent(name, description));
    }
}

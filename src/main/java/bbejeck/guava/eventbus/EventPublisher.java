package bbejeck.guava.eventbus;

import bbejeck.guava.eventbus.events.CashPurchaseEvent;
import bbejeck.guava.eventbus.events.CreditPurchaseEvent;
import bbejeck.guava.eventbus.events.NoSubscriberEvent;
import bbejeck.guava.eventbus.events.SimpleEvent;
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

    public void createCashPurchaseEvent(String description, long amount) {
        eventBus.post(new CashPurchaseEvent(amount, description));
    }

    public void createCreditPurchaseEvent(String item, String ccNumber, long amount) {
        eventBus.post(new CreditPurchaseEvent(amount, ccNumber, item));
    }

    public void createSimpleEvent(String eventName) {
        eventBus.post(new SimpleEvent(eventName));
    }

    public void createNoSubscribedEvent() {
        eventBus.post(new NoSubscriberEvent());
    }

}

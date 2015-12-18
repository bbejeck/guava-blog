package bbejeck.guava.eventbus.subscriber;

import bbejeck.guava.eventbus.events.CreditPurchaseEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 1/6/12
 * Time: 12:14 AM
 */
public class InvalidSubscriberMultipleParameter {


    @Subscribe
    public void handleCreditEvent(CreditPurchaseEvent event, Object foo) {
        //DO nothing this will not work
    }

    public static InvalidSubscriberMultipleParameter instance(EventBus eventBus) {
        InvalidSubscriberMultipleParameter subscriber = new InvalidSubscriberMultipleParameter();
        eventBus.register(subscriber);
        return subscriber;
    }
}

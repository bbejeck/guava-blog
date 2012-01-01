package bbejeck.guava.eventbus.subscriber;

import bbejeck.guava.eventbus.events.CreditPurchaseEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 12/31/11
 * Time: 10:37 PM
 */

public class CreditPurchaseEventSubscriber extends BaseEventSubscriber<CreditPurchaseEvent> {

    public CreditPurchaseEventSubscriber(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    @Subscribe
    public void handleEvent(CreditPurchaseEvent event) {
        events.add(event);
    }
}

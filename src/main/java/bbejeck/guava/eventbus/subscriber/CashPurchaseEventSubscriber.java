package bbejeck.guava.eventbus.subscriber;

import bbejeck.guava.eventbus.events.CashPurchaseEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 12/31/11
 * Time: 10:35 PM
 */

public class CashPurchaseEventSubscriber extends BaseEventSubscriber<CashPurchaseEvent> {

    public CashPurchaseEventSubscriber(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    @Subscribe
    public void handleEvent(CashPurchaseEvent event) {
        events.add(event);
    }
}

package bbejeck.guava.eventbus.subscriber;

import bbejeck.guava.eventbus.events.CashPurchaseEvent;
import com.google.common.eventbus.Subscribe;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 12/31/11
 * Time: 10:35 PM
 */

public class CashPurchaseEventSubscriber extends EventSubscriber<CashPurchaseEvent> {


    @Subscribe
    public void handleEvent(CashPurchaseEvent event) {
        events.add(event);
    }

}

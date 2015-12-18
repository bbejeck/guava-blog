package bbejeck.guava.eventbus.subscriber;

import bbejeck.guava.eventbus.events.PurchaseEvent;
import com.google.common.eventbus.Subscribe;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 12/31/11
 * Time: 10:39 PM
 */

public class PurchaseEventSubscriber extends EventSubscriber<PurchaseEvent> {


    private PurchaseEventSubscriber() {
    }

    @Subscribe
    public void handleEvent(PurchaseEvent event) {
        events.add(event);
    }


}

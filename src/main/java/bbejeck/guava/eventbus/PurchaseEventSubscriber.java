package bbejeck.guava.eventbus;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 12/29/11
 * Time: 11:04 PM
 */
public class PurchaseEventSubscriber {

    EventBus eventBus;
    List<PurchaseEvent> purchaseEvents = new ArrayList<PurchaseEvent>();

    public PurchaseEventSubscriber(EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.register(this);
    }

    @Subscribe
    public void handlePurchaseEvent(PurchaseEvent purchaseEvent) {
        System.out.println("purchaseEvent received = " + purchaseEvent);
        purchaseEvents.add(purchaseEvent);
    }
}

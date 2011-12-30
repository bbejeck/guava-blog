package bbejeck.guava.eventbus;

import com.google.common.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 12/29/11
 * Time: 11:34 PM
 */
public class EventBusTest {

    EventPublisher eventPublisher;
    PurchaseEventSubscriber eventSubscriber;
    EventBus eventBus;

    @Before
    public void setUp() {
        eventBus = new EventBus();
        eventPublisher = new EventPublisher(eventBus);
        eventSubscriber = new PurchaseEventSubscriber(eventBus);
    }

    @Test
    public void testPurchaseEventReceived() {
        eventPublisher.createPurchaseEvent("25000", "Jeep Wrangler");
        assertThat(eventSubscriber.purchaseEvents.size(), is(1));
        PurchaseEvent purchaseEvent = eventSubscriber.purchaseEvents.get(0);
        assertThat(purchaseEvent.amount, is("25000"));
        assertThat(purchaseEvent.item, is("Jeep Wrangler"));
    }

    @Test
    public void testNonPurchaseEventIgnored() {
        eventPublisher.createSimpleEvent("Simple", "Just another event");
        assertThat(eventSubscriber.purchaseEvents.size(), is(0));
    }


}

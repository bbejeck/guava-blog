package bbejeck.guava.eventbus;

import bbejeck.guava.eventbus.events.CashPurchaseEvent;
import bbejeck.guava.eventbus.events.CreditPurchaseEvent;
import bbejeck.guava.eventbus.subscriber.CashPurchaseEventSubscriber;
import bbejeck.guava.eventbus.subscriber.CreditPurchaseEventSubscriber;
import bbejeck.guava.eventbus.subscriber.PurchaseEventSubscriber;
import com.google.common.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 12/29/11
 * Time: 11:34 PM
 */
public class EventBusTest {

    EventPublisher eventPublisher;
    CashPurchaseEventSubscriber cashPurchaseEventSubscriber;
    CreditPurchaseEventSubscriber creditPurchaseEventSubscriber;
    PurchaseEventSubscriber purchaseEventSubscriber;
    EventBus eventBus;

    @Before
    public void setUp() {
        eventBus = new EventBus();
        eventPublisher = new EventPublisher(eventBus);
        cashPurchaseEventSubscriber = new CashPurchaseEventSubscriber(eventBus);
        creditPurchaseEventSubscriber = new CreditPurchaseEventSubscriber(eventBus);
        purchaseEventSubscriber = new PurchaseEventSubscriber(eventBus);
    }

    @Test
    public void testCashPurchaseEventReceived() {
        eventPublisher.createCashPurchaseEvent("Jeep Wrangler", 25000l);
        assertThat(cashPurchaseEventSubscriber.getHandledEvents().size(), is(1));
        assertThat(creditPurchaseEventSubscriber.getHandledEvents().size(), is(0));
        assertSame(cashPurchaseEventSubscriber.getHandledEvents().get(0).getClass(), CashPurchaseEvent.class);
    }

    @Test
    public void testCreditCardPurchaseEventReceived() {
        eventPublisher.createCreditPurchaseEvent("Plane Tickets", "123456789", 25900l);
        assertThat(cashPurchaseEventSubscriber.getHandledEvents().size(), is(0));
        assertThat(creditPurchaseEventSubscriber.getHandledEvents().size(), is(1));
        assertSame(creditPurchaseEventSubscriber.getHandledEvents().get(0).getClass(), CreditPurchaseEvent.class);
    }

    @Test
    public void testGetAllPurchaseEvents() {
        eventPublisher.createCashPurchaseEvent("Jeep Wrangler", 25700l);
        eventPublisher.createCreditPurchaseEvent("Jeep Wrangler", "123456789", 25300l);
        assertThat(purchaseEventSubscriber.getHandledEvents().size(), is(2));

        assertSame(purchaseEventSubscriber.getHandledEvents().get(0).getClass(), CashPurchaseEvent.class);
        assertSame(purchaseEventSubscriber.getHandledEvents().get(1).getClass(), CreditPurchaseEvent.class);
    }

}

package bbejeck.guava.eventbus;

import bbejeck.guava.eventbus.events.CashPurchaseEvent;
import bbejeck.guava.eventbus.events.CreditPurchaseEvent;
import bbejeck.guava.eventbus.events.NoSubscriberEvent;
import bbejeck.guava.eventbus.subscriber.*;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 12/29/11
 * Time: 11:34 PM
 */
public class EventBusTest {

    private EventPublisher eventPublisher;
    private CashPurchaseEventSubscriber cashPurchaseEventSubscriber;
    private CreditPurchaseEventSubscriber creditPurchaseEventSubscriber;
    private PurchaseEventSubscriber purchaseEventSubscriber;
    private EventBus eventBus;
    private AsyncEventBus asyncEventBus;
    private LongProcessSubscriber longProcessSubscriber;
    private DeadEventSubscriber deadEventSubscriber;
    private AllEventSubscriber allEventSubscriber;
    private MultiHandlerSubscriber multiHandlerSubscriber;
    private CountDownLatch doneSignal;
    private int numberLongEvents = 10;


    @Before
    public void setUp() {
        eventBus = new EventBus();
        deadEventSubscriber = new DeadEventSubscriber();
        eventBus.register(deadEventSubscriber);
        eventPublisher = new EventPublisher(eventBus);
        cashPurchaseEventSubscriber = new CashPurchaseEventSubscriber(eventBus);
        creditPurchaseEventSubscriber = new CreditPurchaseEventSubscriber(eventBus);
        purchaseEventSubscriber = new PurchaseEventSubscriber(eventBus);
        multiHandlerSubscriber = new MultiHandlerSubscriber(eventBus);
    }

    @Test
    public void testCashPurchaseEventReceived() {
        generateCashPurchaseEvent();
        assertThat(cashPurchaseEventSubscriber.getHandledEvents().size(), is(1));
        assertThat(creditPurchaseEventSubscriber.getHandledEvents().size(), is(0));
        assertSame(cashPurchaseEventSubscriber.getHandledEvents().get(0).getClass(), CashPurchaseEvent.class);
        assertThat(deadEventSubscriber.deadEvents.size(), is(0));
    }

    @Test
    public void testDeadEvent() {
        eventPublisher.createNoSubscribedEvent();
        assertThat(deadEventSubscriber.deadEvents.size(), is(1));
        DeadEvent deadEvent = deadEventSubscriber.deadEvents.get(0);
        assertSame(deadEvent.getEvent().getClass(), NoSubscriberEvent.class);
    }


    @Test
    public void testCreditCardPurchaseEventReceived() {
        generateCreditPurchaseEvent();
        assertThat(cashPurchaseEventSubscriber.getHandledEvents().size(), is(0));
        assertThat(creditPurchaseEventSubscriber.getHandledEvents().size(), is(1));
        assertSame(creditPurchaseEventSubscriber.getHandledEvents().get(0).getClass(), CreditPurchaseEvent.class);
    }


    @Test
    public void testGetAllPurchaseEvents() {
        generateAllPurchaseEvents();
        assertThat(purchaseEventSubscriber.getHandledEvents().size(), is(2));
        assertSame(purchaseEventSubscriber.getHandledEvents().get(0).getClass(), CashPurchaseEvent.class);
        assertSame(purchaseEventSubscriber.getHandledEvents().get(1).getClass(), CreditPurchaseEvent.class);
    }

    @Test
    public void testUnregisterForEvents() {
        eventBus.unregister(cashPurchaseEventSubscriber);
        CashPurchaseEventSubscriber cashPurchaseEventSubscriber1 = new CashPurchaseEventSubscriber(eventBus);
        CashPurchaseEventSubscriber cashPurchaseEventSubscriber2 = new CashPurchaseEventSubscriber(eventBus);
        eventBus.register(cashPurchaseEventSubscriber1);
        eventBus.register(cashPurchaseEventSubscriber2);

        generateCashPurchaseEvent();
        assertThat(cashPurchaseEventSubscriber.getHandledEvents().size(), is(0));
        assertThat(cashPurchaseEventSubscriber1.getHandledEvents().size(), is(1));
        assertThat(cashPurchaseEventSubscriber2.getHandledEvents().size(), is(1));
    }

    /**
     * Handler for CreditEvent has @AllowConcurrentEvents and each invocation
     * of the handler takes 300 MS, but done in parallel s only takes approximately
     * 300 MS to run
     */
    @Test
    public void testAsyncEventSubscriber() throws Exception {
        asyncEventBus = new AsyncEventBus(Executors.newCachedThreadPool());
        doneSignal = new CountDownLatch(numberLongEvents);
        longProcessSubscriber = new LongProcessSubscriber(asyncEventBus, doneSignal);

        long start = System.currentTimeMillis();
        for (int i = 0; i < numberLongEvents; i++) {
            asyncEventBus.post(new CreditPurchaseEvent(1000l, "Stuff", "1234678"));
        }
        doneSignal.await();
        long elapsed = start - System.currentTimeMillis();
        assertTrue(elapsed <= 300l && elapsed < 500l);
    }

    /**
     * Handler for CashEvent does not @AllowConcurrentEvents and each invocation
     * of the handler takes 1 sec, even though using AsyncEventBus it takes a full
     * 3 seconds to run, so all calls are serial!
     */
    @Test
    public void testNonAsyncEventSubscriber() throws Exception {
        asyncEventBus = new AsyncEventBus(Executors.newCachedThreadPool());
        doneSignal = new CountDownLatch(numberLongEvents);
        longProcessSubscriber = new LongProcessSubscriber(asyncEventBus, doneSignal);

        long start = System.currentTimeMillis();
        for (int i = 0; i < numberLongEvents; i++) {
            asyncEventBus.post(new CashPurchaseEvent(1000l, "Stuff"));
        }
        doneSignal.await();
        long elapsed = start - System.currentTimeMillis();
        assertTrue(elapsed <= 3000l && elapsed < 3500l);
    }

    @Test
    public void testHandleAllEvents() {
        allEventSubscriber = new AllEventSubscriber(eventBus);
        generateAllPurchaseEvents();
        generateSimpleEvent();
        assertThat(allEventSubscriber.getHandledEvents().size(), is(3));
    }

    @Test
    public void testMultiHandlerSubscriber() {
        generateCashPurchaseEvent();
        generateCreditPurchaseEvent();
        generateSimpleEvent();
        assertThat(multiHandlerSubscriber.getCashEvents().size(), is(1));
        assertThat(multiHandlerSubscriber.getCreditEvents().size(), is(1));
        assertThat(multiHandlerSubscriber.getSimpleEvents().size(), is(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultipleParametersInHandler() {
        InvalidSubscriberMultipleParameter invalidSubscriber = new InvalidSubscriberMultipleParameter(eventBus);
        generateCreditPurchaseEvent();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoParametersInHandler() {
        InvalidSubscriberNoParameters invalidSubscriber = new InvalidSubscriberNoParameters(eventBus);
        generateCreditPurchaseEvent();
    }


    private void generateSimpleEvent() {
        eventPublisher.createSimpleEvent("simpleEvent");
    }

    private void generateAllPurchaseEvents() {
        generateCashPurchaseEvent();
        generateCreditPurchaseEvent();
    }

    private void generateCreditPurchaseEvent() {
        eventPublisher.createCreditPurchaseEvent("Plane Tickets", "123456789", 25900l);
    }

    private void generateCashPurchaseEvent() {
        eventPublisher.createCashPurchaseEvent("Jeep Wrangler", 25000l);
    }


    private class DeadEventSubscriber {
        List<DeadEvent> deadEvents = new ArrayList<DeadEvent>();

        @Subscribe
        public void handleDeadEvent(DeadEvent deadEvent) {
            deadEvents.add(deadEvent);
        }

    }
}

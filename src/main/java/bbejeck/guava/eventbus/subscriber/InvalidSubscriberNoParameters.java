package bbejeck.guava.eventbus.subscriber;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 1/6/12
 * Time: 12:21 AM
 */
public class InvalidSubscriberNoParameters {

    private InvalidSubscriberNoParameters() {
    }

    @Subscribe
    public void handleEvent() {
        //DO nothing will not work
    }

    public static InvalidSubscriberNoParameters instance(EventBus eventBus) {
        InvalidSubscriberNoParameters subscriber = new InvalidSubscriberNoParameters();
        eventBus.register(subscriber);
        return subscriber;
    }
}

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

    public InvalidSubscriberNoParameters(EventBus eventBus) {
        eventBus.register(this);
    }

    @Subscribe
    public void handleEvent() {
        //DO nothing will not work
    }
}

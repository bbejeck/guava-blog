package bbejeck.guava.eventbus.subscriber;

import com.google.common.eventbus.EventBus;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 12/31/11
 * Time: 10:04 PM
 */

public abstract class EventSubscriber<T> {

    List<T> events = new ArrayList<T>();

    public List<T> getHandledEvents() {
        return events;
    }


    public static <E extends EventSubscriber> E factory(Class<E> clazz, EventBus eventBus) {
        E subscriber = null;
        try {
            Constructor constructor = clazz.getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            subscriber = (E) constructor.newInstance(new Object[]{});
            eventBus.register(subscriber);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return subscriber;
    }
}

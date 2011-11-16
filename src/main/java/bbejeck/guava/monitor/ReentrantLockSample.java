package bbejeck.guava.monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 11/14/11
 * Time: 9:40 PM
 */
public class ReentrantLockSample {
    private List<String> list = new ArrayList<String>();
    private static final int MAX_SIZE = 10;

    private ReentrantLock rLock = new ReentrantLock();
    private Condition listAtCapacity = rLock.newCondition();

    public void addToList(String item) throws InterruptedException {
        rLock.lock();
        try {
            while (list.size() == MAX_SIZE) {
                listAtCapacity.await();
            }
            list.add(item);
        } finally {
            rLock.unlock();
        }
    }
}

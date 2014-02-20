package bbejeck.guava.striped;

import com.google.common.util.concurrent.Striped;

import java.util.concurrent.Semaphore;

/**
 * User: Bill Bejeck
 * Date: 9/17/13
 * Time: 8:47 PM
 * <p/>
 * This is a simple class to demonstrate using
 * the Guava Striped class
 */
public class ConcurrentWorker {

    private Striped<Semaphore> stripedSemaphores = Striped.semaphore(10, 3);
    private Semaphore semaphore = new Semaphore(3);

    public void stripedConcurrentAccess(String url) throws Exception {
        Semaphore stripedSemaphore = stripedSemaphores.get(url);
        stripedSemaphore.acquire();
        try {
            //Access restricted resource here
            Thread.sleep(25);
        } finally {
            stripedSemaphore.release();
        }
    }

    public void nonStripedConcurrentAccess(String url) throws Exception {
        semaphore.acquire();
        try {
            //Access restricted resource here
            Thread.sleep(25);
        } finally {
            semaphore.release();
        }
    }
}

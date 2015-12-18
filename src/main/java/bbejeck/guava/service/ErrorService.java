package bbejeck.guava.service;

import com.google.common.util.concurrent.AbstractService;

/**
 * User: Bill Bejeck
 * Date: 2/20/14
 * Time: 11:25 PM
 */
public class ErrorService extends AbstractService {
    private boolean throwError = true;

    @Override
    protected void doStart() {
        super.notifyStarted();
        System.out.println("Error service is running now");
        if (throwError) {
            throwError = false;
            throw new RuntimeException("Error");
        }
    }

    @Override
    protected void doStop() {
        super.notifyStopped();
        System.out.println("Error service is stopping now");
    }


}

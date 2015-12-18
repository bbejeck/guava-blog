package bbejeck.guava.service;

import com.google.common.util.concurrent.Service;

/**
 * User: Bill Bejeck
 * Date: 2/20/14
 * Time: 10:59 PM
 */
public class PrintingServiceListener extends Service.Listener {


    @Override
    public void starting() {
        print("Service.Listener: Starting");
    }

    @Override
    public void running() {
        print("Service.Listener: Running");
    }

    @Override
    public void stopping(Service.State from) {
        print("Service.Listener: stopping from state " + from);
    }

    @Override
    public void terminated(Service.State from) {
        print("Service.Listener: terminated from state " + from);
    }

    @Override
    public void failed(Service.State from, Throwable failure) {
        print("Service.Listener: error from state " + from + " with error " + failure);
    }

    private void print(String message) {
        System.out.println(message);
    }
}

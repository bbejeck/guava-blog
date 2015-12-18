package bbejeck.guava.service;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;

import java.util.Date;
import java.util.List;

/**
 * User: Bill Bejeck
 * Date: 2/19/14
 * Time: 11:15 PM
 */
public class ServiceManagerDriver {

    public static void main(String[] args) {

        List<Service> services = Lists.newArrayList();
        SimpleService service = new SimpleService();
        ErrorService errorService = new ErrorService();
        service.addListener(new PrintingServiceListener(), MoreExecutors.sameThreadExecutor());
        errorService.addListener(new PrintingServiceListener(), MoreExecutors.sameThreadExecutor());
        services.add(service);
        services.add(errorService);
        ServiceManager serviceManager = new ServiceManager(services);
        serviceManager.addListener(new ServiceManager.Listener() {
            @Override
            public void healthy() {
                System.out.println("ServiceManager.Listener:Service is healthy");
            }

            @Override
            public void stopped() {
                System.out.println("ServiceManager.Listener:Service has stopped");
            }

            @Override
            public void failure(Service service) {
                System.out.println("ServiceManager.Listener:" + service.failureCause().getMessage());
                System.out.println(service.toString());
            }
        });
        serviceManager.startAsync();
        System.out.println("Service started " + new Date() + " waiting to stop");
        serviceManager.awaitStopped();
        System.out.println("Service stopped " + new Date() + " all done");

    }
}

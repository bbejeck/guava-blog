package bbejeck.guava.service;


import com.google.common.util.concurrent.AbstractExecutionThreadService;

/**
 * User: Bill Bejeck
 * Date: 2/19/14
 * Time: 10:58 PM
 */
public class SimpleService extends AbstractExecutionThreadService {


    @Override
    protected void run() throws Exception {
        int count = 0;
        while (count++ < 1) {
            System.out.println("Simple Service Running....");
            Thread.sleep(300l);
            System.out.println("Doing some tasks ...");
        }
        Thread.sleep(100l);
        System.out.println("All done now");
    }


}

package bbejeck.support;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.Executors;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 11/23/11
 * Time: 2:31 PM
 */
public class BaseSample {

    protected ListeningExecutorService executorService;

    public BaseSample(){
        executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
    }

    public  void shutDown(){
        executorService.shutdownNow();
    }
}

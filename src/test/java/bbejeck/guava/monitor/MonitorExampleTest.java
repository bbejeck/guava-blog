package bbejeck.guava.monitor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.concurrent.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 11/11/11
 * Time: 11:48 PM
 */
public class MonitorExampleTest {

    private MonitorExample monitorExample;
    private ExecutorService executorService;
    private int numberThreads = 10;
    private CountDownLatch startSignal;
    private CountDownLatch doneSignal;

    @Before
    public void setUp() throws Exception {
        monitorExample = new MonitorExample();
        executorService = Executors.newFixedThreadPool(numberThreads);
        startSignal = new CountDownLatch(1);
        doneSignal = new CountDownLatch(numberThreads);
    }

    @After
    public void tearDown() {
        executorService.shutdownNow();
    }

    /*
     * First thread does some simulated work and the following
     * 9 threads will move on.
     */
    @Test
    public void testDemoTryEnterIf() throws Exception {
        setUpThreadsForTestingMethod("demoTryEnterIf");
        startAllThreadsForTest();
        waitForTestThreadsToFinish();
        int expectedTaskCount = 1;
        int expectedSkippedTasks = 9;
        assertThat(monitorExample.getTaskDoneCounter(), is(expectedTaskCount));
        assertThat(monitorExample.getTaskSkippedCounter(), is(expectedSkippedTasks));
    }

    /*
        The first 5 threads will wait for the monitor because
        the guard condition is true, but once it turns false the
        rest of the threads drop off
     */
    @Test
    public void testDemoEnterIfOnlyFiveTasksComplete() throws Exception {
        monitorExample.setStopTaskCount(5);
        setUpThreadsForTestingMethod("demoEnterIf");

        startAllThreadsForTest();
        waitForTestThreadsToFinish();
        int expectedTaskCount = 5;
        int expectedSkippedTasks = 5;

        assertThat(monitorExample.getTaskDoneCounter(), is(expectedTaskCount));
        assertThat(monitorExample.getTaskSkippedCounter(), is(expectedSkippedTasks));

    }

    /*
       All 10 threads enter the monitor as the guard condition
       remains true the entire time.
     */
    @Test
    public void testDemoEnterIfAllTasksComplete() throws Exception {
        monitorExample.setStopTaskCount(Integer.MAX_VALUE);
        setUpThreadsForTestingMethod("demoEnterIf");

        startAllThreadsForTest();
        waitForTestThreadsToFinish();
        int expectedTaskCount = 10;
        int expectedSkippedTasks = 0;

        assertThat(monitorExample.getTaskDoneCounter(), is(expectedTaskCount));
        assertThat(monitorExample.getTaskSkippedCounter(), is(expectedSkippedTasks));

    }

    /*
        Guard condition is initially false, but all 10 threads
        enter the monitor.
     */
    @Test
    public void testDemoEnterWhen() throws Exception {
        monitorExample.setStopTaskCount(Integer.MAX_VALUE);
        monitorExample.setCondition(false);
        setUpThreadsForTestingMethod("demoEnterWhen");
        startAllThreadsForTest();
        int expectedCompletedCount = 0;
        int completedCount = monitorExample.getTaskDoneCounter();
        assertThat(completedCount, is(expectedCompletedCount));

        monitorExample.setCondition(true);

        waitForTestThreadsToFinish();
        expectedCompletedCount = 10;
        completedCount = monitorExample.getTaskDoneCounter();
        assertThat(completedCount, is(expectedCompletedCount));
    }

    /*
      Artificially setting the guard to false after 3 threads complete to demonstrate that
      the remaining 7 threads will wait until the guard condition returns true again and will
      enter the monitor.
     */
    @Test
    public void testDemoEnterWhenAllTasksCompleteEvenWhenConditionChanges() throws Exception {
        monitorExample.setCondition(true);
        monitorExample.setStopTaskCount(3);
        setUpThreadsForTestingMethod("demoEnterWhen");
        startAllThreadsForTest();

        //verifying that only 3 threads have initially worked, re-set the guard to true
        FutureTask<Integer> checkInitialTasksCompleted = new FutureTask<Integer>(
                new Callable<Integer>() {
                    public Integer call() {
                        int initialCompletedTasks = monitorExample.getTaskDoneCounter();
                        monitorExample.setCondition(true);
                        return initialCompletedTasks;

                    }
                });

        new Thread(checkInitialTasksCompleted).start();

        int expectedCompletedCount = 3;
        int completedCount = checkInitialTasksCompleted.get();
        assertThat(completedCount, is(expectedCompletedCount));

        waitForTestThreadsToFinish();
        assertThat(completedCount, is(expectedCompletedCount));
        expectedCompletedCount = 10;
        completedCount = monitorExample.getTaskDoneCounter();
        assertThat(completedCount, is(expectedCompletedCount));
    }

    private void waitForTestThreadsToFinish() throws InterruptedException {
        doneSignal.await(1000l, TimeUnit.MILLISECONDS);
    }

    private void startAllThreadsForTest() {
        startSignal.countDown();
    }

    private Method getMethodUnderTest(String methodName) throws Exception {
        return monitorExample.getClass().getDeclaredMethod(methodName);
    }


    private void setUpThreadsForTestingMethod(String methodName) throws Exception {
        final Method testMethod = getMethodUnderTest(methodName);
        for (int i = 0; i < numberThreads; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        startSignal.await();
                        testMethod.invoke(monitorExample);
                    } catch (Exception e) {
                        //Don't care
                    } finally {
                        doneSignal.countDown();
                    }
                }
            });
        }
    }

}

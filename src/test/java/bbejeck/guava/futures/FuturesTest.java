package bbejeck.guava.futures;

import bbejeck.support.model.Person;
import com.google.common.base.Function;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 11/20/11
 * Time: 2:11 PM
 */

public class FuturesTest extends FuturesTestBase {

    private int numberTasks;
    private CountDownLatch startSignal;
    private CountDownLatch doneSignal;
    private ListeningExecutorService testExecutorService;

    @Before
    public void setUp() throws Exception {
        numberTasks = 5;
        startSignal = new CountDownLatch(1);
        doneSignal = new CountDownLatch(numberTasks);
        testExecutorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
    }

    @After
    public void tearDown() {
        testExecutorService.shutdownNow();
    }

    @Test
    public void testChainSearch() throws Exception {

        Function<List<String>, ListenableFuture<List<Map<String, String>>>> queryFunction = new Function<List<String>, ListenableFuture<List<Map<String, String>>>>() {
            @Override
            public ListenableFuture<List<Map<String, String>>> apply(final List<String> ids) {
                return dataService.getPersonDataByIdAsync(ids);
            }
        };

        ListenableFuture<List<String>> indexSearch = luceneSearcher.searchAsync("(+firstName:martin +lastName:hess)");

        ListenableFuture<List<Map<String, String>>> results = Futures.chain(indexSearch, queryFunction);
        List<Map<String, String>> persons = results.get(1, TimeUnit.SECONDS);
        assertThat(persons.size(), is(1));
        Map<String, String> person = persons.get(0);
        assertThat(person.get("first_name"), is("Martin"));
        assertThat(person.get("last_name"), is("Hess"));
    }

    @Test
    public void testTransformSearch() throws Exception {

        Function<List<String>, List<Person>> transformSearchResults = new Function<List<String>, List<Person>>() {
            @Override
            public List<Person> apply(List<String> ids) {
                return dataService.getPersonsById(ids);
            }
        };

        ListenableFuture<List<String>> indexSearch = luceneSearcher.searchAsync("firstName:martin");
        ListenableFuture<List<Person>> transformedResults = Futures.transform(indexSearch, transformSearchResults);

        List<Person> persons = transformedResults.get(1, TimeUnit.SECONDS);
        int expectedSize = 74;
        assertThat(persons.size(), is(expectedSize));
        for (Person person : persons) {
            assertThat(person.firstName, is("Martin"));
        }
    }

    @Test
    public void allAsListSuccess() throws Exception {
        ListenableFuture<List<Person>> lf1 = getFuture("martin", false);
        ListenableFuture<List<Person>> lf2 = getFuture("bob", false);
        ListenableFuture<List<Person>> lf3 = getFuture("emily", false);
        ListenableFuture<List<Person>> lf4 = getFuture("mona", false);
        ListenableFuture<List<Person>> lf5 = getFuture("tom", false);

        ListenableFuture<List<List<Person>>> lfResults = Futures.allAsList(lf1, lf2, lf3, lf4, lf5);
        startSignal.countDown();
        List<List<Person>> listOfPersonLists = lfResults.get();
        assertThat(listOfPersonLists.size()>0,is(true));
        for(List<Person> personList : listOfPersonLists){
            assertThat(personList.size() > 0,is(true));
        }
    }

    @Test(expected = ExecutionException.class)
    public void allAsListSuccessOneFailure() throws Exception {
        ListenableFuture<List<Person>> lf1 = getFuture("martin", false);
        ListenableFuture<List<Person>> lf2 = getFuture("bob", false);
        ListenableFuture<List<Person>> lf3 = getFuture("emily", true);
        ListenableFuture<List<Person>> lf4 = getFuture("mona", false);
        ListenableFuture<List<Person>> lf5 = getFuture("tom", false);

        ListenableFuture<List<List<Person>>> lfResults = Futures.allAsList(lf1, lf2, lf3, lf4, lf5);
        startSignal.countDown();
        List<List<Person>> listOfPersonLists = lfResults.get();
        fail("should not get here");
    }

    @Test
    public void successfulAsListSuccessOneFailure() throws Exception {
        ListenableFuture<List<Person>> lf1 = getFuture("martin", true);
        ListenableFuture<List<Person>> lf2 = getFuture("bob", false);
        ListenableFuture<List<Person>> lf3 = getFuture("emily", true);
        ListenableFuture<List<Person>> lf4 = getFuture("mona", false);
        ListenableFuture<List<Person>> lf5 = getFuture("tom", false);

        ListenableFuture<List<List<Person>>> lfResults = Futures.successfulAsList(lf1, lf2, lf3, lf4, lf5);
        startSignal.countDown();
        List<List<Person>> listOfPersonLists = lfResults.get();

        assertThat(listOfPersonLists.size()==5,is(true));

        //have null values failed
        assertThat(listOfPersonLists.get(0),is(nullValue()));
        assertThat(listOfPersonLists.get(2),is(nullValue()));

        //succeeded returned valid results
        assertThat(listOfPersonLists.get(1).size() > 0,is(true));
        assertThat(listOfPersonLists.get(3).size() > 0,is(true));
        assertThat(listOfPersonLists.get(4).size() > 0,is(true));
    }


    private ListenableFuture<List<Person>> getFuture(final String firstName, final boolean error) {
        return testExecutorService.submit(new Callable<List<Person>>() {
            @Override
            public List<Person> call() throws Exception {
                startSignal.await();
                if (error) {
                    throw new RuntimeException("Ooops!");
                }
                List<String> ids = luceneSearcher.search("firstName:" + firstName);
                List<Person> persons = dataService.getPersonsById(ids);
                return persons;
            }
        });
    }
}

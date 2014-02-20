package bbejeck.guava.cache;

import bbejeck.guava.futures.SearchingTestBase;
import bbejeck.support.model.Person;
import com.google.common.base.Function;
import com.google.common.cache.*;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 12/13/11
 * Time: 9:12 PM
 */

public class CacheTest extends SearchingTestBase {


    @Test
    public void testCacheWithFastRemovalsAfterWrite() throws Exception {

        PersonListRemovalListener removalListener = new PersonListRemovalListener();

        LoadingCache<String, List<Person>> cache = CacheBuilder.newBuilder().expireAfterWrite(500, TimeUnit.MILLISECONDS)
                .removalListener(removalListener)
                .recordStats()
                .build(getCacheLoader());

        String queryKey = "firstName:bob";
        List<Person> personList = cache.get(queryKey);
        List<Person> personListII = cache.get(queryKey);
        Thread.sleep(500);
        List<Person> personListIII = cache.get(queryKey);
        assertThat(personList == personListII, is(true));
        assertThat(personList == personListIII, is(false));

        RemovalNotification<String, List<Person>> removalNotification = removalListener.getRemovalNotification();
        assertThat(removalNotification.getValue() == personList, is(true));
        assertThat(removalNotification.getCause(), is(RemovalCause.EXPIRED));

        CacheStats stats = cache.stats();
        assertThat(stats.hitCount(), is(1l));
        assertThat(stats.loadCount(), is(2l));
        assertThat(stats.missCount(), is(2l));
    }

    @Test
    public void testCacheLoadedAfterFirstRequestThenCached() throws Exception {
        LoadingCache<String, List<Person>> cache = CacheBuilder.newBuilder().recordStats().build(CacheLoader.from(getFunction()));
        String queryKey = "lastName:smith";
        List<Person> personList = null;
        for (int i = 0; i < 100; i++) {
            personList = cache.get(queryKey);
        }
        CacheStats stats = cache.stats();
        assertThat(stats.hitCount(), is(99l));
        assertThat(stats.loadCount(), is(1l));
        assertThat(stats.missCount(), is(1l));
        assertThat(stats.requestCount(), is(100l));
        assertThat(personList.size(), is(620));
        for (Person person : personList) {
            assertThat(person.lastName, is("Smith"));
        }
    }

    @Test
    public void testCacheSizeLimit() throws Exception {
        PersonListRemovalListener removalListener = new PersonListRemovalListener();
        LoadingCache<String, List<Person>> cache = CacheBuilder.newBuilder()
                .maximumSize(2)
                .removalListener(removalListener)
                .build(CacheLoader.from(getFunction()));
        String queryKey = "lastName:smith";
        String queryKeyII = "firstName:bob";
        String queryKeyIII = "firstName:joe";

        cache.get(queryKey);
        cache.get(queryKeyII);
        cache.get(queryKeyIII);
        assertThat(removalListener.getRemovalNotification().getKey(), is(queryKey));
        assertThat(removalListener.getRemovalNotification().getCause(),is(RemovalCause.SIZE));
    }


    private class PersonListRemovalListener implements RemovalListener<String, List<Person>> {

        private RemovalNotification<String, List<Person>> removalNotification;

        @Override
        public void onRemoval(RemovalNotification<String, List<Person>> removalNotification) {
            this.removalNotification = removalNotification;
        }

        public RemovalNotification<String, List<Person>> getRemovalNotification() {
            return removalNotification;
        }
    }

    private CacheLoader<String, List<Person>> getCacheLoader() {
        return new CacheLoader<String, List<Person>>() {
            @Override
            public List<Person> load(String key) throws Exception {
                List<String> ids = luceneSearcher.search(key);
                return dbService.getPersonsById(ids);
            }
        };
    }

    private Function<String, List<Person>> getFunction() {
        return new Function<String, List<Person>>() {
            @Override
            public List<Person> apply(String searchKey) {
                try {
                    List<String> ids = luceneSearcher.search(searchKey);
                    return dbService.getPersonsById(ids);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

}

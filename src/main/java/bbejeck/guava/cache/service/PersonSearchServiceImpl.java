package bbejeck.guava.cache.service;

import bbejeck.support.database.SampleDBService;
import bbejeck.support.lucene.SampleLuceneSearcher;
import bbejeck.support.model.Person;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 12/10/11
 * Time: 9:33 PM
 */
public class PersonSearchServiceImpl implements SearchService<List<Person>> {

    protected LoadingCache<String, List<Person>> cache;
    private SampleLuceneSearcher luceneSearcher;
    private SampleDBService dbService;

    public PersonSearchServiceImpl(SampleLuceneSearcher luceneSearcher, SampleDBService dbService) {
        this.luceneSearcher = luceneSearcher;
        this.dbService = dbService;
        buildCache();
    }

    @Override
    public List<Person> search(String query) throws Exception {
        return cache.get(query);
    }

    private void buildCache() {
        cache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build(new CacheLoader<String, List<Person>>() {
                    @Override
                    public List<Person> load(String queryKey) throws Exception {
                        List<String> ids = luceneSearcher.search(queryKey);
                        return dbService.getPersonsById(ids);
                    }
                });
    }

    public Cache<String, List<Person>> getCache() {
        return cache;
    }
}

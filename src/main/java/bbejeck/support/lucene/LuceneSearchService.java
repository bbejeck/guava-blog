package bbejeck.support.lucene;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 12/10/11
 * Time: 12:56 PM
 */
public interface LuceneSearchService<T> {

    T find(String query);
}

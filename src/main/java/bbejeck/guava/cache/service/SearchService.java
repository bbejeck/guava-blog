package bbejeck.guava.cache.service;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 12/10/11
 * Time: 9:30 PM
 */
public interface SearchService<T> {

    T search(String query) throws Exception;

}

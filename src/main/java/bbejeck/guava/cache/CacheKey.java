package bbejeck.guava.cache;


import com.google.common.base.Function;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 12/10/11
 * Time: 12:39 PM
 */

public interface CacheKey<F,V> {

    Function<F,V> getLoadFunction();

    V getKeyValue();
}

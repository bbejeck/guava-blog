package bbejeck.guava.cache;

import com.google.common.base.Function;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

import javax.security.auth.callback.CallbackHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 12/8/11
 * Time: 9:33 PM
 */
public class CacheExample {



    public Cache<String,List<String>> buildCache() {
        return CacheBuilder.newBuilder().concurrencyLevel(5)
                .expireAfterWrite(5, TimeUnit.SECONDS)
                .softValues()
                .weakKeys().build(CacheLoader.from(new SimpleFunction()));
    }






    public class SimpleFunction implements Function<String, List<String>>{
        @Override
        public List<String> apply(final String input) {
            List<String> stuff = new ArrayList<String>(){{
                add(input);
                add(input);
                add(input);
            }};
            System.out.println(String.format("Built value, %s must not be in cache",input));
            return stuff;
        }
    }



    public static void main(String[] args) throws Exception {
        CacheExample cacheExample = new CacheExample();
        Cache<String,List<String>> cache = cacheExample.buildCache();
        Map<String,List<String>> map = cache.asMap();
        System.out.println(map.get("bogus"));

        cache.get("foo");
        cache.get("foo");
        cache.get("foo");
        cache.get("bar");
        System.out.println("cache = " + cache.get("foo"));
        Thread.sleep(8000);
        cache.get("foo");
    }


}

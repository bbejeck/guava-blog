package bbejeck.guava.cache.util;

import com.google.common.cache.CacheStats;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 12/10/11
 * Time: 12:42 PM
 */

public class CacheUtils {

    private CacheUtils(){};

    public static CacheStats getCacheStats() {
            return new CacheStats(0,0,0,0,0,0);
    }
}

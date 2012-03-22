package bbejeck.guava.hash;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 3/20/12
 * Time: 9:46 PM
 */

public class BloomFilterTest {

    private BloomFilter<byte[]> bloomFilter;
    private Random random;
    private int numBits;
    private List<BigInteger> stored;
    private List<BigInteger> notStored;

    @Before
    public void setUp() {
        numBits = 128;
        random = new Random();
        stored = new ArrayList<BigInteger>();
        notStored = new ArrayList<BigInteger>();
        loadBigIntList(stored, 1000);
        loadBigIntList(notStored, 100);
    }

    @Test
    public void testMayContain() {
        setUpBloomFilter(stored.size());
        int falsePositiveCount = 0;
        for (BigInteger bigInteger : notStored) {
            boolean mightContain = bloomFilter.mightContain(bigInteger.toByteArray());
            boolean isStored = stored.contains(bigInteger);
            if (mightContain && !isStored) {
                falsePositiveCount++;
            }
        }
        assertThat(falsePositiveCount < 5, is(true));
    }

    @Test
    public void testMayContainGoOverInsertions() {
        setUpBloomFilter(50);
        int falsePositiveCount = 0;
        for (BigInteger bigInteger : notStored) {
            boolean mightContain = bloomFilter.mightContain(bigInteger.toByteArray());
            boolean isStored = stored.contains(bigInteger);
            if (mightContain && !isStored) {
                falsePositiveCount++;
            }
        }
        assertThat(falsePositiveCount, is(notStored.size()));
    }

    private void setUpBloomFilter(int numInsertions) {
        bloomFilter = BloomFilter.create(Funnels.byteArrayFunnel(), numInsertions);
        addStoredBigIntegersToBloomFilter();
    }

    private BigInteger getRandomBigInteger() {
        return new BigInteger(numBits, random);
    }

    private void addStoredBigIntegersToBloomFilter() {
        for (BigInteger bigInteger : stored) {
            bloomFilter.put(bigInteger.toByteArray());
        }
    }

    private void loadBigIntList(List<BigInteger> list, int count) {
        for (int i = 0; i < count; i++) {
            list.add(getRandomBigInteger());
        }
    }

}

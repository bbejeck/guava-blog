package bbejeck.guava.futures;

import bbejeck.support.dataservice.SampleDataService;
import bbejeck.support.lucene.SampleLuceneIndexBuilder;
import bbejeck.support.lucene.SampleLuceneSearcher;
import com.google.common.base.Function;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.apache.lucene.store.RAMDirectory;
import org.h2.tools.Server;
import org.junit.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 11/20/11
 * Time: 2:11 PM
 */

public class FuturesTest {

    private static final String dbUrl = "jdbc:h2:mem:test";
    private static final String insertSql = " CREATE TABLE PERSON(FIRST_NAME VARCHAR(255), LAST_NAME VARCHAR(255)," +
            " ADDRESS VARCHAR(255), EMAIL VARCHAR(255),ID INT PRIMARY KEY) AS SELECT * FROM CSVREAD('src/main/resources/names.csv')";

    private static Connection connection;
    private static Statement statement;
    private static Server dbServer;
    private static SampleLuceneIndexBuilder luceneIndexBuilder;
    private static SampleLuceneSearcher luceneSearcher;
    private static SampleDataService dataService;

    @BeforeClass
    public static void setUpBeforeAllTests() throws Exception {
        startH2();
        setUpIndex();
        connectToDb();
        populateDb();
    }


    @Before
    public void setUp() throws Exception {
        dataService = new SampleDataService(statement);
    }

    @After
    public void tearDown() {
        dataService.shutDown();
    }

    @AfterClass
    public static void shutDownAfterAllTests() throws Exception {
        stopH2();
        closeDbConnection();
        luceneSearcher.shutDown();
    }

    @Test
    public void testChainSearch() throws Exception {

        Function<List<String>, ListenableFuture<List<Map<String, String>>>> queryFunction = new Function<List<String>, ListenableFuture<List<Map<String, String>>>>() {
            @Override
            public ListenableFuture<List<Map<String, String>>> apply(final List<String> ids) {
                return dataService.retrieveDataByRawIdAsync(ids);
            }
        };

        ListenableFuture<List<String>> indexSearch = luceneSearcher.searchAsync("(firstName:martin AND lastName:hess)");

        ListenableFuture<List<Map<String, String>>> results = Futures.chain(indexSearch, queryFunction);
        List<Map<String, String>> persons = results.get(5, TimeUnit.SECONDS);
        assertThat(persons.size(), is(1));
        Map<String, String> person = persons.get(0);
        assertThat(person.get("first_name"), is("Martin"));
        assertThat(person.get("last_name"), is("Hess"));
    }


    private static void populateDb() throws SQLException {
        statement = connection.createStatement();
        statement.execute(insertSql);
    }

    private static void setUpIndex() throws IOException {
        luceneIndexBuilder = new SampleLuceneIndexBuilder();
        RAMDirectory ramDirectory = luceneIndexBuilder.buildIndex();
        luceneSearcher = new SampleLuceneSearcher(ramDirectory);
    }

    private static void startH2() throws Exception {
        dbServer = Server.createTcpServer();
        dbServer.start();
    }

    private static void stopH2() throws Exception {
        dbServer.stop();
    }

    private static void connectToDb() throws Exception {
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection(dbUrl);
    }

    private static void closeDbConnection() throws Exception {
        connection.close();
    }
}

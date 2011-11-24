package bbejeck.support.lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 11/20/11
 * Time: 2:06 PM
 */
public class SampleLuceneSearcher {

    private  IndexSearcher searcher;
    private  QueryParser queryParser;
    private  final int MAX_RESULTS = 1000;

    public SampleLuceneSearcher(RAMDirectory ramDirectory) {
        try {
            searcher = new IndexSearcher(ramDirectory, true);
            queryParser = new QueryParser(Version.LUCENE_34, null, new StandardAnalyzer(Version.LUCENE_34));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> search(String query) throws Exception {
        List<String> results = new ArrayList<String>();
            Query q = queryParser.parse(query);
            TopDocs topDocs = searcher.search(q, MAX_RESULTS);
            for (ScoreDoc sd : topDocs.scoreDocs) {
                Document document = searcher.doc(sd.doc);
                results.add(document.get("id"));
            }
        return results;
    }

}

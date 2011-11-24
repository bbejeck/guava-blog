package bbejeck.support.lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class SampleLuceneIndexBuilder {


    public RAMDirectory buildIndex() throws IOException {
        RAMDirectory ramDirectory = new RAMDirectory();
        String namesFile = "src/main/resources/names.csv";
        Document doc = new Document();
        Field[] fields = new Field[]{new Field("firstName", "", Field.Store.NO, Field.Index.ANALYZED_NO_NORMS),
                new Field("lastName", "", Field.Store.NO, Field.Index.ANALYZED_NO_NORMS),
                new Field("address", "", Field.Store.NO, Field.Index.ANALYZED_NO_NORMS),
                new Field("email", "", Field.Store.NO, Field.Index.ANALYZED_NO_NORMS),
                new Field("id", "", Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS)};
        addFieldsToDocument(doc, fields);

        BufferedReader reader = new BufferedReader(new FileReader(namesFile));

        IndexWriter indexWriter = new IndexWriter(ramDirectory, new IndexWriterConfig(Version.LUCENE_34, new StandardAnalyzer(Version.LUCENE_34)));

        String line;
        while ((line = reader.readLine()) != null) {
            String[] personData = getPersonData(line);
            setFieldData(personData, fields);
            indexWriter.addDocument(doc);
        }
        indexWriter.optimize();
        indexWriter.close();
        return ramDirectory;
    }

    private  String[] getPersonData(String line) {
        return line.split(",");
    }

    private  void setFieldData(String[] data, Field[] fields) {
        int index = 0;
        for (Field field : fields) {
            field.setValue(data[index++]);
        }
    }

    private  void addFieldsToDocument(Document doc, Field[] fields) {
        for (Field field : fields) {
            doc.add(field);
        }
    }

}

package at.swc.lucene;

import java.io.File;
import java.io.IOException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * @author steinwenderp
 */
public final class Lucene3Explain {

    public static void main(String[] args) throws IllegalArgumentException, IOException, ParseException {
        String q = "solr";
        explain("./target/index", q);
    }

    public static void explain(String indexDir, String q) throws IOException, ParseException {
        Directory dir = FSDirectory.open(new File(indexDir));
        DirectoryReader reader = DirectoryReader.open(dir);

        IndexSearcher searcher = new IndexSearcher(reader);

        QueryParser parser = new QueryParser("contents", new StandardAnalyzer());

        Query query = parser.parse(q);

        TopDocs hits = searcher.search(query, 10);

        for (ScoreDoc scoreDoc : hits.scoreDocs) {

            Explanation explaination = searcher.explain(query, scoreDoc.doc);

            Document doc = searcher.doc(scoreDoc.doc);

            System.out.println(doc.get("fullpath"));
            System.out.println(explaination.toString());
        }
        dir.close();
    }

}

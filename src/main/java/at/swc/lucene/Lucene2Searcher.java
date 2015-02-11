package at.swc.lucene;

import java.io.File;
import java.io.IOException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * @author steinwenderp
 */
public final class Lucene2Searcher {

    public static void main(String[] args) throws IllegalArgumentException, IOException, ParseException {
        String q = "solr";
        search("./target/index", q);
    }

    public static void search(String indexDir, String q) throws IOException, ParseException {
        Directory dir = FSDirectory.open(new File(indexDir));
        DirectoryReader reader = DirectoryReader.open(dir);

        IndexSearcher searcher = new IndexSearcher(reader);

        QueryParser parser = new QueryParser("contents", new StandardAnalyzer());

        Query query = parser.parse(q);

        long start = System.currentTimeMillis();
        TopDocs hits = searcher.search(query, 10);
        long end = System.currentTimeMillis();

        System.err.println("Found " + hits.totalHits +
                " document(s) (in " + (end - start) +
                " milliseconds) that matched query '" +
                q + "':");

        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println(doc.get("fullpath"));
        }
        dir.close();
    }

}

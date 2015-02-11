package at.swc.lucene;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * @author steinwenderp
 */
public final class Lucene1Indexer {

    public static void main(String[] args) throws Exception {
        Lucene1Indexer indexer = new Lucene1Indexer("./target/index");
        indexer.index("docs");
        indexer.close();
    }

    private IndexWriter indexWriter;

    public Lucene1Indexer(String indexRoot) throws IOException {
        FSDirectory dir = FSDirectory.open(new File(indexRoot));
        IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_4_10_3, new StandardAnalyzer());
        indexWriter = new IndexWriter(dir, conf);
    }

    public int index(String docsDir) throws Exception {
        for (File file : new File(docsDir).listFiles()) {
            indexFile(file);
        }
        return indexWriter.numDocs();
    }

    private void indexFile(File f) throws Exception {
        System.out.println("Indexing " + f.getCanonicalPath());
        Document doc = getDocument(f);
        indexWriter.addDocument(doc);
    }

    private Document getDocument(File f) throws Exception {
        Document doc = new Document();
        doc.add(new TextField("contents", new FileReader(f))); // when using Reader, value does not get stored
        doc.add(new StringField("filename", f.getName(), Field.Store.YES));
        doc.add(new StringField("fullpath", f.getCanonicalPath(), Field.Store.YES));
        return doc;
    }

    private void close() throws IOException {
        indexWriter.close();
    }

}

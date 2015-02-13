package at.swc.solr;

import java.util.Arrays;
import java.util.List;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.beans.Field;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;

/**
 * @author steinwenderp
 */
public final class Solr3Entity {

    public static class Doc {
        @Field
        String id;
        @Field
        String name;
        @Field("cat")
        String[] cats;

        public Doc() {
        }

        public Doc(String id, String name, String... cats) {
            this.id = id;
            this.name = name;
            this.cats = cats;
        }

        @Override
        public String toString() {
            return "Doc{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", cats=" + Arrays.toString(cats) +
                    '}';
        }
    }

    public static void main(String[] args) throws Exception {

        SolrServer solrServer = new HttpSolrServer("http://localhost:8983/solr/collection1");

        solrServer.addBean(new Doc("id", "name", "cat1", "cat2"));
        solrServer.commit();

        QueryResponse response = solrServer.query(new SolrQuery("name:name"));
        List<Doc> docs = response.getBeans(Doc.class);
        for (Doc doc : docs) {
            System.out.println(doc);
        }

    }

}

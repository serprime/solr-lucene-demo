package at.swc.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

/**
 * @author steinwenderp
 */
public final class Solr1Client {

    /**
     * Before execution:
     * <p/>
     * Start a solr instance from the examples:
     * <p/>
     * eg:
     * solr-dist/bin/solr -e cloud -noprompt
     */
    public static void main(String[] args) throws Exception {

        SolrServer solrServer = new HttpSolrServer("http://localhost:8983/solr/collection1");

        QueryResponse response = solrServer.query(new SolrQuery("*:*"));

        System.out.println("total: " + response.getResults().getNumFound());
        for (SolrDocument document : response.getResults()) {
            document.getFieldValue(F.NAME);
        }


    }

    private static class F {
        public static final String NAME = "name";
    }

}

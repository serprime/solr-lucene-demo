package at.swc.solr;

import java.util.Collection;
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

        SolrQuery query = new SolrQuery("*:*");

        query.setRows(100);

        QueryResponse response = solrServer.query(query);

        System.out.println("total: " + response.getResults().getNumFound());

        System.out.println("results: ");
        for (SolrDocument document : response.getResults()) {

            Object name = document.getFieldValue(Fields.NAME);

            if (name == null) {
                continue;
            }

            System.out.print(name);

            Collection<Object> fieldValues = document.getFieldValues(Fields.CAT);
            if (fieldValues != null) {
                for (Object fieldValue : fieldValues) {
                    System.out.print(" (" + fieldValue + ")");
                }
            }
            
            System.out.println("");
        }
    }

}

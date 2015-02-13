package at.swc.solr;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

/**
 * @author steinwenderp
 */
public final class Solr2Add {

    public static void main(String[] args) throws Exception {

        SolrServer solrServer = new HttpSolrServer("http://localhost:8983/solr/collection1");

        SolrInputDocument doc = new SolrInputDocument();

        doc.setField(Fields.ID, "someId");
        doc.addField(Fields.CAT, "book");
        doc.addField(Fields.CAT, "game");
        doc.setField(Fields.NAME, "whateva");

        solrServer.add(doc);

        solrServer.commit();

    }

}

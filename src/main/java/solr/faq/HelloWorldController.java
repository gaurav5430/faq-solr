package solr.faq;

import java.io.IOException;
import java.util.List;

import model.FAQ;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class HelloWorldController {

	@Autowired
	SolrClient client;
	
	@RequestMapping(value ="/index", method= RequestMethod.POST)
	public String index(@RequestBody List<FAQ> faqList) throws SolrServerException, IOException{
		for(int i=0; i<faqList.size(); i++){
			SolrInputDocument doc = new SolrInputDocument();
			doc.addField("question", faqList.get(i).getQuestion());
			doc.addField("answer", faqList.get(i).getAnswer());
			for(int j=0;j<faqList.get(i).getConcept().size();j++){
				doc.addField("concept", faqList.get(i).getConcept().get(j));
			}
			
			client.add(doc);
		}
		
		client.commit();
		
		
		return "indexed";
	}
	
    @RequestMapping(value="/search", method = RequestMethod.GET)
    public String search(@RequestParam("query") String queryText){
    	SolrQuery query = new SolrQuery();
        query.setQuery(queryText);
        query.setFields("question", "answer");
        query.set("defType", "dismax");
        query.set("qf", "question^10.0 answer^1.0");
        QueryResponse response = null;
        try {
        	response = client.query(query);
        }
        catch(Exception ex){
        	System.out.println(ex.getMessage());
        }
        
        SolrDocumentList results = response.getResults();
        System.out.println(results.size());
        for (int i = 0; i < results.size(); ++i) {
            System.out.println(results.get(i));
        }
        
        if(results.size() > 0) return results.get(0).getFieldValue("answer").toString();
        return "";
    }
}
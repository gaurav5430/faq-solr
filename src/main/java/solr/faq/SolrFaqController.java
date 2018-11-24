package solr.faq;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.FAQ;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import utils.Utils;



@RestController
public class SolrFaqController {

	@Autowired
	SolrClient client;
	
	static private SolrDocument getJaccardSimilarity(String query, SolrDocumentList docs) {
		double topSimilarity = -1;
		SolrDocument topDoc = null;
		for(SolrDocument doc : docs) {
			double similarity = Utils.similarity(
					Utils.removeStopwords(Utils.tokenize(doc.getFieldValue("question").toString())), 
					Utils.removeStopwords(Utils.tokenize(query)));
			if(similarity > topSimilarity) {
				topDoc = doc;
				topSimilarity = similarity;
			}
		}
		
		return topDoc;
	}
	
	
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
    public String search(@RequestParam("query") String queryText) throws UnsupportedEncodingException{
    	SolrQuery query = new SolrQuery();
    	String decodedQuery = URLDecoder.decode(queryText, StandardCharsets.UTF_8.toString());
    	System.out.println(decodedQuery);
        query.setQuery(decodedQuery);
        query.setFields("question", "answer");
        query.set("defType", "dismax");
        query.set("qf", "question^10 answer^2");
        QueryResponse response = null;
        try {
        	response = client.query(query);
        }
        catch(Exception ex){
        	System.out.println(ex.getMessage());
        }
        
        SolrDocumentList results = response.getResults();
        SolrDocumentList forJaccard = new SolrDocumentList();
        System.out.println(results.size());
        for (int i = 0; i < results.size(); ++i) {
        	//for maximum top5 get the jaccard similarity to rerank and return the top
        	if(i<5) forJaccard.add(results.get(i));
            System.out.println(results.get(i));
        }
        
        SolrDocument top = getJaccardSimilarity(decodedQuery, forJaccard);
        
        if(top!=null) return top.getFieldValue("answer").toString();
        return "";
    }
}
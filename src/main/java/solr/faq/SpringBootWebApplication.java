package solr.faq;


import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
@SpringBootApplication
public class SpringBootWebApplication {
	
	@Value(("${solr.url}"))
	private String solrUrl;
	private SolrClient solrClient;
	
    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebApplication.class, args);
    }
    
    @Bean
    public SolrClient getSolrClient() {
		solrClient = new HttpSolrClient.Builder(solrUrl).build(); 
		return solrClient;
	}	
}
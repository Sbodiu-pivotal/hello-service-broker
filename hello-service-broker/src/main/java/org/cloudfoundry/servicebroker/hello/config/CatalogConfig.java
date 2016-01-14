package org.cloudfoundry.servicebroker.hello.config;

import org.springframework.cloud.servicebroker.model.Catalog;
import org.springframework.cloud.servicebroker.model.Plan;
import org.springframework.cloud.servicebroker.model.ServiceDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class CatalogConfig {
	
	@Bean
	public Catalog catalog() {
		return new Catalog( Arrays.asList(
				new ServiceDefinition(
					"hello", 
					"hello-service", 
					"A multi-language hello service", 
					true, 
					false,
					Arrays.asList(
							new Plan("english",
									"english", 
									"English language hello plan",
									getPlanMetadata(), true)
							),
					Arrays.asList("hello", "document"),
					getServiceDefinitionMetadata(),
					null,
					null))
				);
	}
	
/* Used by Pivotal CF console */	
	
	private Map<String,Object> getServiceDefinitionMetadata() {
		Map<String,Object> sdMetadata = new HashMap<>();
		sdMetadata.put("displayName", "hello");
		sdMetadata.put("longDescription","Hello Service in English");
		sdMetadata.put("providerDisplayName","Pivotal");
		return sdMetadata;
	}
	
	private Map<String,Object> getPlanMetadata() {		
		Map<String,Object> planMetadata = new HashMap<>();
		planMetadata.put("costs", getCosts());
		planMetadata.put("bullets", getBullets());
		planMetadata.put("context","en");
		return planMetadata;
	}
	
	@SuppressWarnings("unchecked")
	private List<Map<String,Object>> getCosts() {
		Map<String,Object> costsMap = new HashMap<>();

		Map<String,Object> amount = new HashMap<>();
		amount.put("usd", new Double(0.0));
	
		costsMap.put("amount", amount);
		costsMap.put("unit", "MONTHLY");
		
		return Arrays.asList(costsMap);
	}
	
	private List<String> getBullets() {
		return Arrays.asList("Shared Hello server", 
				"0 MB Storage (not enforced)", 
				"10 concurrent connections (not enforced)");
	}
	
}
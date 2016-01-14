package org.cloudfoundry.hello;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import feign.Feign;

@Profile("test")
@Configuration
public class TestConfig {

	private static final String URI = "http://localhost:8080/hello/en";

	@Bean
	public HelloRepository helloRepository() {
		return Feign.builder().target(HelloRepository.class, URI);
	}
}

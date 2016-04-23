package org.cloudfoundry.hello;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

@SpringBootApplication
public class HelloService {

	public static void main(String[] args) {
		SpringApplication.run(HelloService.class, args);
	}

	@Bean(name = "hellos")
	public PropertiesFactoryBean hellos() {
		PropertiesFactoryBean bean = new PropertiesFactoryBean();
		bean.setLocation(new ClassPathResource("hellos.properties"));
		return bean;
	}
}

@RestController
class HelloController {

	@Resource(name = "hellos")
	private Map<String, String> hellos;

	@RequestMapping(value = "/hello/{lang}", method = RequestMethod.GET)
	public Greeting sayHello(@PathVariable String lang) {
		Greeting greeting = new Greeting();
		if (lang != null && hellos.containsKey(lang)) {
			greeting.setGreeting(hellos.get(lang));
		}
		return greeting;
	}

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public Set<String> languages() {
		return hellos.keySet();
	}
}

class Greeting {

	private String greeting;

	public String getGreeting() {
		return greeting;
	}

	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}

}

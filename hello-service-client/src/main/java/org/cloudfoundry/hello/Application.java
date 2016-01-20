package org.cloudfoundry.hello;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import feign.Feign;
import feign.RequestLine;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}

@Profile("cloud")
@Configuration
class CloudConfig {
	@Bean
	public HelloRepository helloRepository() {
		return Feign.builder().target(HelloRepository.class,
				HelloController.getURIFromVCAP(System.getenv("VCAP_SERVICES")));
	}
}

@RestController
class HelloController {

	private static final Logger LOG = Logger.getLogger(HelloController.class);

	@Autowired
	HelloRepository helloRepository;

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public String sayHello() {
		return helloRepository.sayHello();
	}

	public static String getURIFromVCAP(String vcapJson) {
		JsonElement contents = new JsonParser().parse(vcapJson)
				.getAsJsonObject().get("hello-service");

		JsonElement credentials = null;
		if (contents.isJsonArray()) {
			credentials = contents.getAsJsonArray().get(0).getAsJsonObject().get("credentials");
		} else {
			credentials = contents.getAsJsonObject().get("credentials");
		}

		String s = credentials.getAsJsonObject().get("uri").getAsString();
		LOG.info("Service URI from VCAP_SERVICES is: " + s);

		return s;
	}
}

@Repository
interface HelloRepository {

	@RequestLine("GET /")
	String sayHello();
}
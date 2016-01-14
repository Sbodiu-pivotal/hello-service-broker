package org.cloudfoundry.servicebroker.hello;

import org.cloudfoundry.servicebroker.hello.service.HelloClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.servicebroker.model.BrokerApiVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public HelloClient helloClientClient() {
        return new HelloClient();
    }

    @Bean
    public BrokerApiVersion brokerApiVersion() {
        return new BrokerApiVersion();
    }

    @Autowired
    private Environment env;

    @Bean
    public String serviceUri() {
        return env.getProperty("SERVICE_URI", "http://localhost:8080/hello");
    }

}
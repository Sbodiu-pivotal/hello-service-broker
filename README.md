#Hello Service Broker

Sample Spring Boot project using the [Spring Cloud - Cloud Foundry Service Broker](https://github.com/spring-cloud/spring-cloud-cloudfoundry-service-broker).

# Overview

This sample project implements a service broker for the [hello-lang-service](https://github.com/cf-platform-eng/hello-lang-service).
 
## Getting Started

Build the service

Build and Run it locally 

    cd hello-lang-service
    mvn clean package 
    java -jar target/hello-lang-service-1.0.0.jar

Push the service to cloud foundry

    cf push -b java_buildpack

Build and Push hello-service-broker

    pushd; cd hello-service-broker
    mvn clean package
    cf push -b java_buildpack

Register Service Broker with Cloud Foundry

    cf create-service-broker Hello user password http://hello-service-broker.local.micropcf.io
    cf m
    cf service-brokers
    cf enable-service-access hello-service
    cf m
    cf cs hello-service english hello-service

Build and Push hello-client

    pushd; cd hello-client
    mvn clean package
    cf push -b java_buildpack
    open http://hello-client.local.micropcf.io/hello

Spring Security by default re-generate a new password every time:

    cf logs hello-service-broker --recent | grep password
    cf m
    cf set-env hello-service-broker security.user.password password
    cf restage hello-service-broker

Note you can change SERVICE_URI for any other deployment 

    cf set-env hello-service-broker SERVICE_URI http://hello-lang-service.local.micropcf.io/hello
    cf env hello-service-broker
    cf restage hello-service-broker

After building, you can push the broker app to Cloud Foundry or deploy it some other way and then [register it to Cloud Foundry](http://docs.cloudfoundry.org/services/managing-service-brokers.html#register-broker).

## Cleanup

Note if it Fails you will need to clear the db
cf purge-service-instance or cf purge-service-offering 

cf purge-service-offering -f hello-service


    cf d -f hello-client
    cf ds -f hello-service
    cf disable-service-access hello-service
    cf delete-service-broker -f Hello
    cf d -f hello-service-broker
    cf d -f hello-lang-service

## hello-lang-service

Different language "plans" can be added to the catalog and apps can then bind to these to get a "hello" in the language of the plan.

Simple service that says hello in different languages.

To add languages, edit the hellos.properties


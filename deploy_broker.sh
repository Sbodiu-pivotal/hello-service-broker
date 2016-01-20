#!/bin/bash
set -e
# Push Service and Service Broker
cf push -f hello-service/manifest.yml -b java_buildpack
cf push -f hello-service-broker/manifest.yml -b java_buildpack
# Register Service Broker with Cloud Foundry
cf create-service-broker Hello user password http://hello-service-broker.local.micropcf.io
cf m
cf service-brokers
cf enable-service-access hello-service
cf m
# Create Service and Push hello-service-client
cf cs hello-service english hello-service
cf push -f hello-service-client/manifest.yml -b java_buildpack
open http://hello-service-client.local.micropcf.io/hello

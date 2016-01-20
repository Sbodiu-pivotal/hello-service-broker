#!/bin/bash
cf d -f hello-service-client
cf ds -f hello-service
cf disable-service-access hello-service
cf delete-service-broker -f Hello
cf d -f hello-service-broker
cf d -f hello-service
package org.cloudfoundry.servicebroker.hello.service;

import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceRequest;
import org.springframework.cloud.servicebroker.model.DeleteServiceInstanceRequest;
import org.springframework.cloud.servicebroker.model.UpdateServiceInstanceRequest;

import java.util.HashMap;
import java.util.Map;

public class TestFixture {

    public static CreateServiceInstanceRequest getCreateServiceInstanceRequest() {
        String organizationGuid = "orgid";
        String planId = "en";
        String serviceDefinitionId = "hello";
        String spaceGuid = "spaceid";

        return new CreateServiceInstanceRequest(serviceDefinitionId, planId, organizationGuid, spaceGuid)
                .withServiceInstanceId("abc");
    }

    public static CreateServiceInstanceBindingRequest getCreateServiceInstanceBindingRequest() {
        String serviceDefinitionId = "hello";
        String planId = "en";
        String appGuid = "app123";
        Map<String, Object> boundResource = new HashMap<>();
        return new CreateServiceInstanceBindingRequest(serviceDefinitionId, planId, appGuid, boundResource)
                .withBindingId("xyz")
                .withServiceInstanceId("abc");
    }

    public static ServiceInstance getCreateServiceInstance() {
        return new ServiceInstance(getCreateServiceInstanceRequest());
    }

    public static DeleteServiceInstanceRequest getDeleteServiceInstanceRequest() {
        return new DeleteServiceInstanceRequest("abc", "hello", "en", null);
    }

    public static UpdateServiceInstanceRequest getUpdateServiceInstanceRequest() {
       return new UpdateServiceInstanceRequest("hello", "fr")
               .withServiceInstanceId("abc");

    }
}

package org.cloudfoundry.servicebroker.hello.service;

import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TestFixture {
    public static ServiceInstanceBinding getServiceInstanceBinding() {
        Map<String, Object> credentials = Collections.singletonMap("url", (Object) "mongo://example.com");
        return new ServiceInstanceBinding("binding-id", "service-instance-id", credentials, null, "app-guid");
    }

    public static ServiceInstance getServiceInstance() {
        return new ServiceInstance("service-instance-id", "service-definition-id", "plan-id",
                "org-guid", "space-guid", "http://dashboard.example.com");
    }

    public static CreateServiceInstanceRequest getCreateServiceInstanceRequest() {
        String organizationGuid = "orgid";
        String planId = "english";
        String serviceDefinitionId = "hello";
        String spaceGuid = "spaceid";

        return new CreateServiceInstanceRequest(serviceDefinitionId, planId, organizationGuid, spaceGuid)
                .withServiceInstanceId("abc");
    }

    public static CreateServiceInstanceBindingRequest getCreateServiceInstanceBindingRequest() {
        String serviceDefinitionId = "hello";
        String planId = "english";
        String appGuid = "app123";
        Map<String, Object> boundResource = new HashMap<>();
        return new CreateServiceInstanceBindingRequest(serviceDefinitionId, planId, appGuid, boundResource)
                .withBindingId("xyz")
                .withServiceInstanceId("abc");
    }

    public static ServiceInstance getCreateServiceInstance() {
        return new ServiceInstance(getCreateServiceInstanceRequest());
    }
}

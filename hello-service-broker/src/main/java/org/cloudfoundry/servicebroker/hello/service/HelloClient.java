package org.cloudfoundry.servicebroker.hello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.exception.ServiceBrokerException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceDoesNotExistException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceExistsException;
import org.springframework.cloud.servicebroker.model.Plan;
import org.springframework.cloud.servicebroker.model.ServiceDefinition;
import org.springframework.cloud.servicebroker.service.CatalogService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HelloClient {

    @Autowired
    private CatalogService catalogService;

    @Autowired
	private String serviceUri;

	private final ConcurrentHashMap<String, ServiceInstanceBinding> bindings = new ConcurrentHashMap<>();

	private final ConcurrentHashMap<String, ServiceInstance> instances = new ConcurrentHashMap<>();

    public Plan get(String serviceDefinitionId, String planId) {
        ServiceDefinition serviceDefinition = catalogService.getServiceDefinition(serviceDefinitionId);
        if (serviceDefinition == null) {
            return null;
        }
        for (Plan p : serviceDefinition.getPlans()) {
            if (p.getId().equals(planId)) {
                return p;
            }
        }
        return null;
    }

	public ServiceInstance createInstanceIfAbsent(ServiceInstance instance)
			throws ServiceBrokerException, ServiceInstanceExistsException {
		return instances.putIfAbsent(instance.getServiceInstanceId(), instance);
	}

	public ServiceInstance deleteInstance(String serviceInstanceId )
			throws ServiceInstanceDoesNotExistException, ServiceBrokerException {
        return instances.remove(serviceInstanceId);
	}

    public void updateInstance(ServiceInstance serviceInstance) {
        instances.put(serviceInstance.getServiceInstanceId(), serviceInstance);
    }

	public ServiceInstance getInstance(String id) {
		return instances.get(id);
	}

    public Map<String, Object> bindService(String bindingId, String serviceInstanceId, String boundAppGuid) {
        ServiceInstance si = instances.get(serviceInstanceId);

        Plan plan = get(si.getServiceDefinitionId(), si.getPlanId());

        String context = (String) plan.getMetadata().get("context");

        Map<String, Object> credentials = new HashMap<>();
        credentials.put("uri", serviceUri + "/" + context);

        ServiceInstanceBinding binding = new ServiceInstanceBinding(bindingId,
                serviceInstanceId, credentials, null, boundAppGuid);

        bindings.put(bindingId, binding);
        return credentials;
    }

    public void deleteBinding(String bindingId) {
        bindings.remove(bindingId);
    }

    public ServiceInstanceBinding getBinding(String id) {
        return bindings.get(id);
    }

    void cleanup() {
        bindings.clear();
        instances.clear();
    }
}

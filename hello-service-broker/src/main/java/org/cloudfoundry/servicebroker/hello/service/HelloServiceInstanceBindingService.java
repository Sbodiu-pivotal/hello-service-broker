package org.cloudfoundry.servicebroker.hello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.exception.ServiceBrokerException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceBindingDoesNotExistException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceBindingExistsException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceDoesNotExistException;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceAppBindingResponse;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.service.ServiceInstanceBindingService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class HelloServiceInstanceBindingService implements ServiceInstanceBindingService {

	@Autowired
	private HelloClient helloClient;

	@Override
    public CreateServiceInstanceAppBindingResponse createServiceInstanceBinding(
			CreateServiceInstanceBindingRequest request)
			throws ServiceBrokerException,
			ServiceInstanceBindingExistsException {

        String bindingId = request.getBindingId();
        String serviceInstanceId = request.getServiceInstanceId();

        if (serviceInstanceId == null || bindingId == null) {
			throw new ServiceBrokerException("invalid CreateServiceInstanceBindingRequest object.");
		}

        ServiceInstance instance = helloClient.getInstance(serviceInstanceId);
        if (instance == null) {
            throw new ServiceInstanceDoesNotExistException(serviceInstanceId);
        }

		ServiceInstanceBinding binding = helloClient.getBinding(bindingId);
		if (binding != null) {
			throw new ServiceInstanceBindingExistsException(serviceInstanceId, bindingId);
		}

        Map<String, Object> credentials = helloClient
                .bindService(bindingId, serviceInstanceId, request.getAppGuid());

		return new CreateServiceInstanceAppBindingResponse()
					.withCredentials(credentials);
	}

	@Override
	public void deleteServiceInstanceBinding(
			DeleteServiceInstanceBindingRequest request)
			throws ServiceBrokerException, ServiceInstanceDoesNotExistException {
        String bindingId = request.getBindingId();
        if (bindingId == null) {
			throw new ServiceBrokerException("invalid DeleteServiceInstanceRequest object.");
		}

		ServiceInstanceBinding i = helloClient.getBinding(bindingId);
		if (i == null) {
			throw new ServiceInstanceBindingDoesNotExistException(bindingId);
		}

        helloClient.deleteBinding(bindingId);
	}
}

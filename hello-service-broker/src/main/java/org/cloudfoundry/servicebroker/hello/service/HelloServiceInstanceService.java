package org.cloudfoundry.servicebroker.hello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.exception.ServiceBrokerException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceDoesNotExistException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceExistsException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceUpdateNotSupportedException;
import org.springframework.cloud.servicebroker.model.*;
import org.springframework.cloud.servicebroker.service.ServiceInstanceService;
import org.springframework.stereotype.Service;

@Service
public class HelloServiceInstanceService implements ServiceInstanceService {

	@Autowired
	HelloClient helloClient;

	@Override
	public CreateServiceInstanceResponse createServiceInstance(
			CreateServiceInstanceRequest request)
			throws ServiceInstanceExistsException, ServiceBrokerException {
		// TODO hello dashboard
        planExists(request.getServiceDefinitionId(), request.getPlanId());

        ServiceInstance instance = helloClient.createInstanceIfAbsent(new ServiceInstance(request));
        if (instance != null) {
            throw new ServiceInstanceExistsException(request.getServiceInstanceId(), request.getServiceDefinitionId());
        }

        return new CreateServiceInstanceResponse();
	}

    @Override
	public GetLastServiceOperationResponse getLastOperation(GetLastServiceOperationRequest request) {
		return new GetLastServiceOperationResponse(OperationState.SUCCEEDED);
	}

	@Override
	public DeleteServiceInstanceResponse deleteServiceInstance(
			DeleteServiceInstanceRequest request) {
        String serviceInstanceId = request.getServiceInstanceId();

        ServiceInstance instance = helloClient.getInstance(serviceInstanceId);
        if (instance == null) {
            throw new ServiceInstanceDoesNotExistException(serviceInstanceId);
        }

		helloClient.deleteInstance(serviceInstanceId);

		return new DeleteServiceInstanceResponse();
	}

	@Override
	public UpdateServiceInstanceResponse updateServiceInstance(
			UpdateServiceInstanceRequest request)
			throws ServiceInstanceUpdateNotSupportedException,
			ServiceBrokerException, ServiceInstanceDoesNotExistException {
		String serviceInstanceId = request.getServiceInstanceId();

		ServiceInstance instance = helloClient.getInstance(serviceInstanceId);
		if (instance == null) {
			throw new ServiceInstanceDoesNotExistException(serviceInstanceId);
		}

		planExists(request.getServiceDefinitionId(), request.getPlanId());

		helloClient.updateInstance(new ServiceInstance(instance, request));

		return new UpdateServiceInstanceResponse();
	}

    private void planExists(String serviceDefinitionId, String planId) {
        Plan plan = helloClient.get(serviceDefinitionId, planId);
        if (plan == null) {
            throw new ServiceBrokerException("service plan " + planId + " not supported by this broker.");
        }
    }
}
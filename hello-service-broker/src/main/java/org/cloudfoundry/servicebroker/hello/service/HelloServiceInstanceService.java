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
        String serviceInstanceId = request.getServiceInstanceId();
        String serviceId = request.getServiceDefinitionId();
        String planId = request.getPlanId();

        if (serviceId == null || planId == null || serviceInstanceId == null) {
            throw new ServiceBrokerException("invalid CreateServiceInstanceRequest object.");
        }

        ServiceDefinition serviceDefinition = helloClient.get(serviceId);
        if (serviceDefinition == null) {
            throw new ServiceBrokerException("service " + serviceId + " not supported by this broker.");
        }

        Plan plan = helloClient.get(serviceDefinition, planId);
        if (plan == null) {
            throw new ServiceBrokerException("service plan " + planId + " not supported by this broker.");
        }

        ServiceInstance instance = helloClient.createInstanceIfAbsent(new ServiceInstance(request));
        if (instance != null) {
            throw new ServiceInstanceExistsException(serviceInstanceId, request.getServiceDefinitionId());
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
        if (serviceInstanceId == null) {
            throw new ServiceBrokerException("invalid DeleteServiceInstanceRequest object.");
        }

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
		// TODO not Supported

		return new UpdateServiceInstanceResponse();
	}
}
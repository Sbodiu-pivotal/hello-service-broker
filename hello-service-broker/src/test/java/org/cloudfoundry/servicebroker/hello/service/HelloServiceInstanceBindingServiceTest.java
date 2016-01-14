package org.cloudfoundry.servicebroker.hello.service;

import org.cloudfoundry.servicebroker.hello.Application;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.cloud.servicebroker.exception.ServiceBrokerException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceBindingDoesNotExistException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceBindingExistsException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceDoesNotExistException;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingResponse;
import org.springframework.cloud.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class })
public class HelloServiceInstanceBindingServiceTest {

    @Autowired
    private HelloServiceInstanceBindingService service;

    @Autowired
    private HelloClient client;

    private ServiceInstance instance = TestFixture.getCreateServiceInstance();

    @After
    public void cleanup() {
        client.cleanup();
    }

    @Test
    public void newServiceInstanceBindingCreatedSuccessfully() throws ServiceBrokerException,
            ServiceInstanceBindingExistsException {
        client.createInstanceIfAbsent(instance);
        CreateServiceInstanceBindingRequest c = TestFixture.getCreateServiceInstanceBindingRequest();
        CreateServiceInstanceBindingResponse sib = service.createServiceInstanceBinding(c);
        assertNotNull(sib);

        Map<String, Object> m = sib.getCredentials();
        assertNotNull(m);
        assertEquals("http://localhost:8080/hello/en", m.get("uri"));

        ServiceInstanceBinding instanceBinding = client.getBinding(c.getBindingId());
        assertEquals("app123", instanceBinding.getAppGuid());
        assertEquals("xyz", instanceBinding.getId());
        assertEquals("abc", instanceBinding.getServiceInstanceId());
    }

    @Test(expected = ServiceInstanceDoesNotExistException.class)
    public void serviceInstanceCreationFailsWithExistingInstance() throws Exception {
        CreateServiceInstanceBindingRequest c = TestFixture.getCreateServiceInstanceBindingRequest();
        CreateServiceInstanceBindingResponse sib = service.createServiceInstanceBinding(c);
    }

    @Test
    public void serviceInstanceBindingDeletedSuccessfully() throws Exception {
        client.createInstanceIfAbsent(instance);
        CreateServiceInstanceBindingRequest c = TestFixture.getCreateServiceInstanceBindingRequest();
        client.bindService(c.getBindingId(), c.getServiceInstanceId(), c.getBoundAppGuid());

        service.deleteServiceInstanceBinding(
                buildDeleteRequest(c.getBindingId()));
    }

    @Test(expected = ServiceInstanceBindingDoesNotExistException.class)
    public void unknownServiceInstanceDeleteCallSuccessful() throws Exception {
        service.deleteServiceInstanceBinding(
                buildDeleteRequest("unknown"));
    }

    private DeleteServiceInstanceBindingRequest buildDeleteRequest(String bindingId) {
        return new DeleteServiceInstanceBindingRequest(instance.getServiceInstanceId(), bindingId,
                instance.getServiceDefinitionId(), instance.getPlanId(), null);
    }
}
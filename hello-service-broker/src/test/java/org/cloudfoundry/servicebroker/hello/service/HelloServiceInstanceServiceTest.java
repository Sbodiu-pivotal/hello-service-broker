package org.cloudfoundry.servicebroker.hello.service;

import org.cloudfoundry.servicebroker.hello.Application;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.cloud.servicebroker.exception.ServiceBrokerException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceDoesNotExistException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceExistsException;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceRequest;
import org.springframework.cloud.servicebroker.model.DeleteServiceInstanceRequest;
import org.springframework.cloud.servicebroker.model.UpdateServiceInstanceRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class })
public class HelloServiceInstanceServiceTest {

    @Autowired
    HelloServiceInstanceService service;

    @Autowired
    HelloClient client;

    @After
    public void cleanup() {
        client.cleanup();
    }

    @Test
    public void newServiceInstanceCreatedSuccessfully() throws ServiceBrokerException,
            ServiceInstanceExistsException {
        CreateServiceInstanceRequest req = TestFixture.getCreateServiceInstanceRequest();
        service.createServiceInstance(req);

        ServiceInstance si = client.getInstance(req.getServiceInstanceId());
        assertNotNull(si);
        assertEquals("orgid", si.getOrganizationGuid());
        assertEquals("en", si.getPlanId());
        assertEquals("hello", si.getServiceDefinitionId());
        assertEquals("abc", si.getServiceInstanceId());
        assertEquals("spaceid", si.getSpaceGuid());
    }

    @Test(expected=ServiceInstanceExistsException.class)
    public void serviceInstanceRequiredInstance() throws Exception {
        ServiceInstance instance = TestFixture.getCreateServiceInstance();
        client.createInstanceIfAbsent(instance);

        CreateServiceInstanceRequest req = TestFixture.getCreateServiceInstanceRequest();
        service.createServiceInstance(req);
    }

    @Test
    public void serviceInstanceDeletedSuccessfully() throws Exception {
        CreateServiceInstanceRequest create = TestFixture.getCreateServiceInstanceRequest();
        service.createServiceInstance(create);
        ServiceInstance si = client.getInstance(create.getServiceInstanceId());
        assertNotNull(si);

        DeleteServiceInstanceRequest req = TestFixture.getDeleteServiceInstanceRequest();
        service.deleteServiceInstance(req);
        si = client.getInstance(create.getServiceInstanceId());
        assertNull(si);
    }

    @Test
    public void serviceInstanceUpdatedSuccessfully() throws Exception {
        CreateServiceInstanceRequest create = TestFixture.getCreateServiceInstanceRequest();
        service.createServiceInstance(create);
        ServiceInstance si = client.getInstance(create.getServiceInstanceId());
        assertNotNull(si);

        UpdateServiceInstanceRequest req = TestFixture.getUpdateServiceInstanceRequest();
        service.updateServiceInstance(req);
        si = client.getInstance(create.getServiceInstanceId());
        assertNotNull(si);
        assertEquals("orgid", si.getOrganizationGuid());
        assertEquals("fr", si.getPlanId());
        assertEquals("hello", si.getServiceDefinitionId());
        assertEquals("abc", si.getServiceInstanceId());
        assertEquals("spaceid", si.getSpaceGuid());
    }

    @Test(expected = ServiceInstanceDoesNotExistException.class)
    public void unknownServiceInstanceDeleteCallSuccessful() throws Exception {
        CreateServiceInstanceRequest create = TestFixture.getCreateServiceInstanceRequest();
        DeleteServiceInstanceRequest req = new DeleteServiceInstanceRequest(
                create.getServiceInstanceId(), "hello", "english", create.getServiceDefinition());
        service.deleteServiceInstance(req);
        ServiceInstance si = client.getInstance(create.getServiceInstanceId());
        assertNull(si);
    }

}
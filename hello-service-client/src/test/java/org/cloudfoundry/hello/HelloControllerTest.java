package org.cloudfoundry.hello;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@WebIntegrationTest(value = "server.port=9873")
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class })
public class HelloControllerTest {

	@Autowired
	HelloController helloController;

	@Test
	public void testHello() {
		assertEquals("{\"greeting\":\"Hello\"}", helloController.sayHello());
	}

	@Test
	public void testParseVCAP() throws Exception {
		String s = "{\"hello-service\":[{\"name\":\"hello-service\",\"label\":\"hello-service\",\"tags\":[\"hello\",\"document\"],\"plan\":\"english\",\"credentials\":{\"uri\":\"http://localhost:8080/hello/en/\"}}]}";
		String uri = HelloController.getURIFromVCAP(s);
		assertNotNull(uri);
		assertEquals("http://localhost:8080/hello/en/",uri);
	}
}

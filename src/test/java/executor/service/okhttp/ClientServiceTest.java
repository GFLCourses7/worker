package executor.service.okhttp;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import executor.service.config.BeanConfig;
import executor.service.config.PropertiesConfigHolder;
import executor.service.model.*;
import executor.service.security.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientServiceTest {

    @Mock
    private PropertiesConfigHolder propertiesConfigHolder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFetchScenario() throws IOException {

        Long id = 1L;
        String name = "test_name";
        String site = "http://info.cern.ch";
        String result = "INFO: test_result";
        String stepAction = "sleep";
        String stepValue = "5000";

        String expectedJson = String.format("""
                {
                    "id": %s,
                    "name": "%s",
                    "site": "%s",
                    "result": "%s",
                    "steps": [
                        {
                            "action": "%s",
                            "value": "%s"
                        }
                    ]
                }
                """, id, name, site, result, stepAction, stepValue);

        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(expectedJson));
        server.enqueue(new MockResponse().setBody(expectedJson));
        server.enqueue(new MockResponse().setBody(expectedJson));
        server.start();

        ScenarioWrapper expected = new ScenarioWrapper();
        expected.setId(id);
        expected.setName(name);
        expected.setSite(site);
        expected.setResult(result);
        expected.setSteps(Arrays.stream(new Step[] {new Step(stepAction, stepValue)}).toList());
        BeanConfig beanConfig = new BeanConfig(propertiesConfigHolder);

        ClientService proxyClientService = new ClientService(
                "http://" + server.getHostName(),
                server.getPort(),
                beanConfig.getOkHttpClientBean(),
                beanConfig.getObjectMapperBean());

        ScenarioWrapper actual = (ScenarioWrapper) proxyClientService.fetchScenario();

        assertEquals(expected, actual);

        server.shutdown();

    }

    @Test
    public void testFetchProxy() throws IOException {

        String proxyHost = "host";
        Integer proxyPort = 8080;
        String proxyUsername = "username";
        String proxyPassword = "password";

        String expectedJson = String.format("""
                {
                    "proxyNetworkConfig": {
                      "hostname": "%s",
                      "port": %s
                    },
                    "proxyCredentials": {
                      "username": "%s",
                      "password": "%s"
                    }
                }
                """, proxyHost, proxyPort, proxyUsername, proxyPassword);

        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(expectedJson));
        server.enqueue(new MockResponse().setBody(expectedJson));
        server.enqueue(new MockResponse().setBody(expectedJson));
        server.start();

        ProxyConfigHolder expected = new ProxyConfigHolder(
                new ProxyNetworkConfig(proxyHost, proxyPort),
                new ProxyCredentials(proxyUsername, proxyPassword)
        );

        BeanConfig beanConfig = new BeanConfig(propertiesConfigHolder);

        ClientService proxyClientService = new ClientService(
                "http://" + server.getHostName(),
                server.getPort(),
                beanConfig.getOkHttpClientBean(),
                beanConfig.getObjectMapperBean());

        ProxyConfigHolder actual = proxyClientService.fetchProxy();

        assertEquals(expected, actual);

        server.shutdown();
    }

    @Test
    public void testSendResult() throws IOException {

        String expected = "200";

        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(expected));
        server.enqueue(new MockResponse().setBody(expected));
        server.enqueue(new MockResponse().setBody(expected));
        server.start();

        ScenarioWrapper scenario = new ScenarioWrapper(1L, "TEST_RESULT");

        BeanConfig beanConfig = new BeanConfig(propertiesConfigHolder);

        ClientService proxyClientService = new ClientService(
                "http://" + server.getHostName(),
                server.getPort(),
                beanConfig.getOkHttpClientBean(),
                beanConfig.getObjectMapperBean());

        boolean success = proxyClientService.sendResult(scenario);

        server.shutdown();

        assertTrue(success);
    }

    @Test
    public void testLogin() throws IOException {

        String expected = "test_token";
        String actual;

        LoginResponse loginResponse = new LoginResponse(expected);

        BeanConfig beanConfig = new BeanConfig(propertiesConfigHolder);

        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(beanConfig.getObjectMapperBean().writeValueAsString(loginResponse)));
        server.enqueue(new MockResponse().setBody(beanConfig.getObjectMapperBean().writeValueAsString(loginResponse)));
        server.enqueue(new MockResponse().setBody(beanConfig.getObjectMapperBean().writeValueAsString(loginResponse)));
        server.start();

        ScenarioWrapper scenario = new ScenarioWrapper(1L, "TEST_RESULT");



        ClientService proxyClientService = new ClientService(
                "http://" + server.getHostName(),
                server.getPort(),
                beanConfig.getOkHttpClientBean(),
                beanConfig.getObjectMapperBean());

        proxyClientService.login();

        actual = proxyClientService.jwtToken;

        server.shutdown();

        assertEquals(expected, actual);
    }

}

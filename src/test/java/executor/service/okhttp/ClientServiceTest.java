package executor.service.okhttp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import executor.service.config.BeanConfig;
import executor.service.model.ProxyConfigHolder;
import executor.service.model.ProxyCredentials;
import executor.service.model.ProxyNetworkConfig;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientServiceTest {

    @Test
    public void testFetchScenario() {
        // To do ...
    }

    @Test
    public void testGetProxy() throws IOException {

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

        BeanConfig beanConfig = new BeanConfig();

        ClientService proxyClientService = new ClientService(
                "http://" + server.getHostName(),
                server.getPort(),
                beanConfig.getOkHttpClientBean(),
                beanConfig.getObjectMapperBean()
        );

        ProxyConfigHolder actual = proxyClientService.getProxy();

        assertEquals(expected, actual);

        server.shutdown();
    }

}

package executor.service.config.proxy;

import executor.service.model.ProxyConfigHolder;
import executor.service.model.ProxyCredentials;
import executor.service.model.ProxyNetworkConfig;
import executor.service.utils.JsonConfigReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mockStatic;

class ProxySourcesClientLoaderTest {
    private ProxySourcesClient proxySourcesClient;
    private ProxyConfigHolder fakeProxyConfigHolder;
    private ProxyNetworkConfig fakeProxyNetworkConfig;
    private ProxyCredentials fakeProxyCredentials;

    @BeforeEach
    void setUp() {
        fakeProxyNetworkConfig = new ProxyNetworkConfig("Fake hostname 1", 1010);
        fakeProxyCredentials = new ProxyCredentials("Fake Username 1", "Strong Password");

        fakeProxyConfigHolder = new ProxyConfigHolder(fakeProxyNetworkConfig, fakeProxyCredentials);
    }

    @Test
    public void testGetProxy() {
        try (MockedStatic<JsonConfigReader> utilities = mockStatic(JsonConfigReader.class)) {
            utilities.when(() -> JsonConfigReader.readFile(eq("ProxyNetwork.json"), any()))
                    .thenReturn(List.of(fakeProxyNetworkConfig));
            utilities.when(() -> JsonConfigReader.readFile(eq("ProxyCredentials.json"), any()))
                    .thenReturn(List.of(fakeProxyCredentials));

            proxySourcesClient = new ProxySourcesClientLoader();
            ProxyConfigHolder proxy = proxySourcesClient.getProxy();
            assertEquals(fakeProxyConfigHolder, proxy);
        }
    }

    @Test
    public void testGetProxyThrowsNoSuchElementException() {
        try (MockedStatic<JsonConfigReader> utilities = mockStatic(JsonConfigReader.class)) {
            utilities.when(() -> JsonConfigReader.readFile(eq("ProxyNetwork.json"), any()))
                    .thenReturn(Collections.emptyList());
            utilities.when(() -> JsonConfigReader.readFile(eq("ProxyCredentials.json"), any()))
                    .thenReturn(Collections.emptyList());

            proxySourcesClient = new ProxySourcesClientLoader();

            assertThrows(NoSuchElementException.class, () -> proxySourcesClient.getProxy());
        }
    }

}
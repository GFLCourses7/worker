package executor.service.config.proxy;

import executor.service.model.ProxyConfigHolder;
import executor.service.model.ProxyCredentials;
import executor.service.model.ProxyNetworkConfig;
import executor.service.utils.configreader.ConfigReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class ProxySourcesClientLoaderTest {

    @Mock
    private ConfigReader configReader;

    private ProxySourcesClientLoader proxySourcesClientLoader;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetProxy_WithProxiesAvailable() {
        ProxyConfigHolder proxyConfigHolder = new ProxyConfigHolder(new ProxyNetworkConfig(), new ProxyCredentials());

        List<ProxyConfigHolder> proxies = new ArrayList<>();
        proxies.add(proxyConfigHolder);

        when(configReader.readFile(anyString(), eq(ProxyConfigHolder.class))).thenReturn(proxies);

        proxySourcesClientLoader = new ProxySourcesClientLoader(configReader);

        ProxyConfigHolder result = proxySourcesClientLoader.getProxy();

        assertEquals(proxyConfigHolder, result);
    }

    @Test
    public void testGetProxy_WithNoProxiesAvailable() {
        when(configReader.readFile(anyString(), eq(ProxyConfigHolder.class))).thenReturn(new ArrayList<>());

        proxySourcesClientLoader = new ProxySourcesClientLoader(configReader);

        ProxyConfigHolder result = proxySourcesClientLoader.getProxy();

        assertNull(result);
    }

    @Test
    public void testAddProxy() {
        ProxyConfigHolder proxyConfigHolder = new ProxyConfigHolder(new ProxyNetworkConfig(), new ProxyCredentials());

        proxySourcesClientLoader = new ProxySourcesClientLoader(configReader);
        proxySourcesClientLoader.addProxy(proxyConfigHolder);

        ProxyConfigHolder proxy = proxySourcesClientLoader.getProxy();
        assertEquals(proxyConfigHolder, proxy);
    }
}

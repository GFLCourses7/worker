package executor.service.config.proxy;

import executor.service.model.ProxyConfigHolder;
import executor.service.model.ProxyCredentials;
import executor.service.model.ProxyNetworkConfig;
import executor.service.okhttp.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class ProxySourcesClientOkHttpTest {

    @Mock
    private ClientService clientService;

    private ProxySourcesClientOkHttp proxySourcesClientOkHttp;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        proxySourcesClientOkHttp = new ProxySourcesClientOkHttp(clientService);
    }

    @Test
    void testGetProxy() {
        ProxyConfigHolder fakeProxyConfigHolder = new ProxyConfigHolder();
        fakeProxyConfigHolder.setProxyCredentials(new ProxyCredentials("username", "password"));
        fakeProxyConfigHolder.setProxyNetworkConfig(new ProxyNetworkConfig("hostname", 1010));

        when(clientService.fetchProxy()).thenReturn(fakeProxyConfigHolder);

        ProxyConfigHolder proxy = proxySourcesClientOkHttp.getProxy();
        assertNotNull(proxy);
        assertEquals(fakeProxyConfigHolder, proxy);
    }
}
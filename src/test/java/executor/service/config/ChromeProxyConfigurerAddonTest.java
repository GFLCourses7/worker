package executor.service.config;

import executor.service.model.ProxyConfigHolder;
import executor.service.model.ProxyCredentials;
import executor.service.model.ProxyNetworkConfig;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class ChromeProxyConfigurerAddonTest {

    @Test
    public void testAddExtensionsCallback() throws IOException {

        ChromeOptions chromeOptions = mock(ChromeOptions.class);

        ProxyConfigHolder proxyConfigHolder = mock(ProxyConfigHolder.class);
        when(proxyConfigHolder.getProxyNetworkConfig()).thenReturn(mock(ProxyNetworkConfig.class));
        when(proxyConfigHolder.getProxyCredentials()).thenReturn(mock(ProxyCredentials.class));

        ProxyConfigFileInitializer proxyConfigFileInitializer = mock(ProxyConfigFileInitializer.class);
        when(proxyConfigFileInitializer.initProxyConfigFile(any(),any(),any(),any())).thenReturn(mock(File.class));

        ChromeProxyConfigurerAddon chromeProxyConfigurerAddon
                = new ChromeProxyConfigurerAddon(() -> proxyConfigFileInitializer);

        chromeProxyConfigurerAddon.configureProxy(chromeOptions, proxyConfigHolder);

        verify(chromeOptions).addExtensions(any(File.class));
    }

    @Test
    public void testInitProxyConfigFileCallback() throws IOException {

        ChromeOptions chromeOptions = mock(ChromeOptions.class);

        ProxyConfigHolder proxyConfigHolder = mock(ProxyConfigHolder.class);
        when(proxyConfigHolder.getProxyNetworkConfig()).thenReturn(mock(ProxyNetworkConfig.class));
        when(proxyConfigHolder.getProxyCredentials()).thenReturn(mock(ProxyCredentials.class));

        ProxyConfigFileInitializer proxyConfigFileInitializer = mock(ProxyConfigFileInitializer.class);
        when(proxyConfigFileInitializer.initProxyConfigFile(any(),any(),any(),any())).thenReturn(mock(File.class));

        ChromeProxyConfigurerAddon chromeProxyConfigurerAddon
                = new ChromeProxyConfigurerAddon(() -> proxyConfigFileInitializer);

        chromeProxyConfigurerAddon.configureProxy(chromeOptions, proxyConfigHolder);

        verify(proxyConfigHolder, times(2)).getProxyNetworkConfig();
        verify(proxyConfigHolder, times(2)).getProxyCredentials();

    }

}

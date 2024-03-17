package executor.service.config;

import executor.service.model.ProxyConfigHolder;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;

public class ChromeProxyConfigurerAddon implements ChromeProxyConfigurer {

    @Override
    public Runnable configureProxy(ChromeOptions options, ProxyConfigHolder proxyConfigHolder) throws IOException {

        ProxyConfigFileInitializer proxyFileConfigInitializer = new ProxyConfigFileInitializer();

        File configZip = proxyFileConfigInitializer.initProxyConfigFile(
                proxyConfigHolder.getProxyNetworkConfig().getHostname(),
                proxyConfigHolder.getProxyNetworkConfig().getPort(),
                proxyConfigHolder.getProxyCredentials().getUsername(),
                proxyConfigHolder.getProxyCredentials().getPassword()
        );

        options.addExtensions(configZip);

        return proxyFileConfigInitializer::clearDirectory;
    }
}

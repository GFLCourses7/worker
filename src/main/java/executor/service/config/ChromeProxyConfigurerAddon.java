package executor.service.config;

import executor.service.model.ProxyConfigHolder;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class ChromeProxyConfigurerAddon implements ChromeProxyConfigurer {

    private final ObjectFactory<ProxyConfigFileInitializer> proxyConfigFileInitializerObjectFactory;

    public ChromeProxyConfigurerAddon(ObjectFactory<ProxyConfigFileInitializer> proxyConfigFileInitializerObjectFactory) {
        this.proxyConfigFileInitializerObjectFactory = proxyConfigFileInitializerObjectFactory;
    }

    @Override
    public Runnable configureProxy(ChromeOptions options, ProxyConfigHolder proxyConfigHolder) throws IOException {

        ProxyConfigFileInitializer proxyFileConfigInitializer = proxyConfigFileInitializerObjectFactory.getObject();

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

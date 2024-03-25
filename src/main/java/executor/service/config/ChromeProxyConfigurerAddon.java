package executor.service.config;

import executor.service.model.ProxyConfigHolder;
import org.openqa.selenium.chrome.ChromeOptions;
import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

public class ChromeProxyConfigurerAddon implements ChromeProxyConfigurer {

    private final Supplier<ProxyConfigFileInitializer> proxyConfigFileInitializerSupplier;

    public ChromeProxyConfigurerAddon(Supplier<ProxyConfigFileInitializer> proxyConfigFileInitializerSupplier) {
        this.proxyConfigFileInitializerSupplier = proxyConfigFileInitializerSupplier;
    }

    @Override
    public Runnable configureProxy(ChromeOptions options, ProxyConfigHolder proxyConfigHolder) throws IOException {

        ProxyConfigFileInitializer proxyFileConfigInitializer = proxyConfigFileInitializerSupplier.get();

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

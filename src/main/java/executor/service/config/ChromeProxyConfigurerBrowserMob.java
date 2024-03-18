package executor.service.config;

import executor.service.model.ProxyConfigHolder;
import executor.service.model.ProxyCredentials;
import executor.service.model.ProxyNetworkConfig;
import executor.service.webdriver.ChromeDriverInitializer;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.auth.AuthType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;

import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChromeProxyConfigurerBrowserMob implements ChromeProxyConfigurer {

    private static final Logger LOGGER = Logger.getLogger(ChromeDriverInitializer.class.getName());

    @Override
    public Runnable configureProxy(ChromeOptions options, ProxyConfigHolder proxyConfigHolder) {

        Proxy proxy = getProxy(proxyConfigHolder);
        options.setCapability(CapabilityType.PROXY, proxy);
        LOGGER.log(Level.INFO, String.format("Proxy configured: %s:%d", proxyConfigHolder.getProxyNetworkConfig().getHostname(), proxyConfigHolder.getProxyNetworkConfig().getPort()));

        return () -> {
            // Do nothing, since there is no memory to clear
        };
    }

    private Proxy getProxy(ProxyConfigHolder proxyConfigHolder) {
        LOGGER.log(Level.INFO, "Configuring proxy...");

        ProxyNetworkConfig proxyNetworkConfig = proxyConfigHolder.getProxyNetworkConfig();
        ProxyCredentials proxyCredentials = proxyConfigHolder.getProxyCredentials();

        BrowserMobProxyServer proxy = new BrowserMobProxyServer();

        proxy.setChainedProxy(new InetSocketAddress(proxyNetworkConfig.getHostname(), proxyNetworkConfig.getPort()));

        if (proxyConfigHolder.getProxyCredentials()!=null)
            proxy.chainedProxyAuthorization(proxyCredentials.getUsername(), proxyCredentials.getPassword(), AuthType.BASIC);

        proxy.start(0);

        LOGGER.log(Level.INFO, "Proxy configured successfully");
        return ClientUtil.createSeleniumProxy(proxy);
    }
}

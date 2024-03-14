package executor.service.webdriver;

import executor.service.config.proxy.ProxySourcesClientLoader;
import executor.service.model.ProxyConfigHolder;
import executor.service.model.ProxyCredentials;
import executor.service.model.ProxyNetworkConfig;
import executor.service.model.WebDriverConfig;
import executor.service.config.PropertiesConfigHolder;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.auth.AuthType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChromeDriverInitializer implements WebDriverInitializer {
    private static final Logger LOGGER = Logger.getLogger(ChromeDriverInitializer.class.getName());

    @Override
    public WebDriver init() {
        LOGGER.log(Level.INFO, "Initializing WebDriver...");

        WebDriverConfig webDriverConfig = PropertiesConfigHolder.loadConfigFromFile();
        ProxyConfigHolder proxyConfigHolder = new ProxySourcesClientLoader().getProxy();

        // Set WebDriver executable
        System.setProperty("webdriver.chrome.driver", webDriverConfig.getWebDriverExecutable());

        // Configure Chrome options
        ChromeOptions options = new ChromeOptions();

        // Set user agent if provided
        if (webDriverConfig.getUserAgent() != null) {
            options.addArguments("--user-agent=" + webDriverConfig.getUserAgent());
        }

        // Set proxy if provided
        if (proxyConfigHolder != null && proxyConfigHolder.getProxyNetworkConfig() != null) {
            Proxy proxy = getProxy(proxyConfigHolder);
            options.setCapability(CapabilityType.PROXY, proxy);
            LOGGER.log(Level.INFO, String.format("Proxy configured: %s:%d", proxyConfigHolder.getProxyNetworkConfig().getHostname(), proxyConfigHolder.getProxyNetworkConfig().getPort()));
        }

        // Initialize ChromeDriver
        ChromeDriver driver = new ChromeDriver(options);

        // Configure timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(webDriverConfig.getImplicitlyWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofMillis(webDriverConfig.getPageLoadTimeout()));

        LOGGER.log(Level.INFO, "WebDriver initialized successfully.");
        return driver;
    }

    private static Proxy getProxy(ProxyConfigHolder proxyConfigHolder) {
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

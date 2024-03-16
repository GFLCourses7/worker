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

        WebDriverConfig webDriverConfig = loadWebDriverConfig();
        ProxyConfigHolder proxyConfigHolder = loadProxyConfig();

        ChromeOptions options = configureChromeOptions(webDriverConfig);

        if (proxyConfigHolder != null && proxyConfigHolder.getProxyNetworkConfig() != null) {
            configureProxy(options, proxyConfigHolder);
        }

        ChromeDriver driver = createChromeDriver(options);
        configureTimeouts(driver, webDriverConfig);

        LOGGER.log(Level.INFO, "WebDriver initialized successfully.");
        return driver;
    }

    protected WebDriverConfig loadWebDriverConfig() {
        return PropertiesConfigHolder.loadConfigFromFile();
    }

    protected ProxyConfigHolder loadProxyConfig() {
        return new ProxySourcesClientLoader().getProxy();
    }

    protected ChromeOptions configureChromeOptions(WebDriverConfig webDriverConfig) {
        ChromeOptions options = new ChromeOptions();
        System.setProperty("webdriver.chrome.driver", webDriverConfig.getWebDriverExecutable());

        if (webDriverConfig.getUserAgent() != null) {
            options.addArguments("--user-agent=" + webDriverConfig.getUserAgent());
        }

        return options;
    }

    protected void configureProxy(ChromeOptions options, ProxyConfigHolder proxyConfigHolder) {
        Proxy proxy = getProxy(proxyConfigHolder);
        options.setCapability(CapabilityType.PROXY, proxy);
        LOGGER.log(Level.INFO, String.format("Proxy configured: %s:%d", proxyConfigHolder.getProxyNetworkConfig().getHostname(), proxyConfigHolder.getProxyNetworkConfig().getPort()));
    }

    protected ChromeDriver createChromeDriver(ChromeOptions options) {
        return new ChromeDriver(options);
    }

    protected void configureTimeouts(WebDriver driver, WebDriverConfig webDriverConfig) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(webDriverConfig.getImplicitlyWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofMillis(webDriverConfig.getPageLoadTimeout()));
    }

    protected Proxy getProxy(ProxyConfigHolder proxyConfigHolder) {
        LOGGER.log(Level.INFO, "Configuring proxy...");

        ProxyNetworkConfig proxyNetworkConfig = proxyConfigHolder.getProxyNetworkConfig();
        ProxyCredentials proxyCredentials = proxyConfigHolder.getProxyCredentials();

        BrowserMobProxyServer proxy = new BrowserMobProxyServer();
        proxy.setChainedProxy(new InetSocketAddress(proxyNetworkConfig.getHostname(), proxyNetworkConfig.getPort()));

        if (proxyConfigHolder.getProxyCredentials() != null) {
            proxy.chainedProxyAuthorization(proxyCredentials.getUsername(), proxyCredentials.getPassword(), AuthType.BASIC);
        }

        proxy.start(0);
        LOGGER.log(Level.INFO, "Proxy configured successfully");
        return ClientUtil.createSeleniumProxy(proxy);
    }
}

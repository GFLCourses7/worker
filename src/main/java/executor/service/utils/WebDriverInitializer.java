package executor.service.utils;

import executor.service.model.ProxyConfigHolder;
import executor.service.model.ProxyCredentials;
import executor.service.model.ProxyNetworkConfig;
import executor.service.model.WebDriverConfig;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebDriverInitializer {
    private static final Logger LOGGER = Logger.getLogger(WebDriverInitializer.class.getName());

    public WebDriver init(WebDriverConfig webDriverConfig, ProxyConfigHolder proxyConfigHolder) {
        LOGGER.log(Level.INFO, "Initializing WebDriver...");

        // Set WebDriver executable
        System.setProperty("webdriver.chrome.driver", webDriverConfig.getWebDriverExecutable());

        // Configure Chrome options
        ChromeOptions options = new ChromeOptions();

        // Set user agent if provided
        if (webDriverConfig.getUserAgent() != null) {
            options.addArguments("--user-agent=" + webDriverConfig.getUserAgent());
        }

        // Set proxy if provided
        if (proxyConfigHolder != null && proxyConfigHolder.getProxyNetworkConfig() != null && proxyConfigHolder.getProxyCredentials() != null) {
            Proxy proxy = getProxy(proxyConfigHolder);
            options.setProxy(proxy);
            LOGGER.log(Level.INFO, "Proxy configured: " + proxyConfigHolder.getProxyNetworkConfig().getHostname() + ":" + proxyConfigHolder.getProxyNetworkConfig().getPort());
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

        String proxyAddress = String.format("%s:%d", proxyNetworkConfig.getHostname(), proxyNetworkConfig.getPort());
        String proxyAuth = String.format("%s:%s", proxyCredentials.getUsername(), proxyCredentials.getPassword());
        String proxyAddressAndAuth = String.format("%s@%s", proxyAuth, proxyAddress);

        Proxy proxy = new Proxy();
        proxy.setProxyType(Proxy.ProxyType.MANUAL);
        proxy.setHttpProxy(proxyAddressAndAuth);
        proxy.setSslProxy(proxyAddressAndAuth);

        LOGGER.log(Level.INFO, "Proxy configured successfully: " + proxyAddressAndAuth);
        return proxy;
    }
}

package executor.service.utils;

import executor.service.config.proxy.ProxySourcesClientLoader;
import executor.service.model.ProxyConfigHolder;
import executor.service.model.ProxyCredentials;
import executor.service.model.ProxyNetworkConfig;
import executor.service.model.WebDriverConfig;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebDriverInitializer {
    private static final Logger LOGGER = Logger.getLogger(WebDriverInitializer.class.getName());

    public WebDriver init() {
        LOGGER.log(Level.INFO, "Initializing WebDriver...");

        WebDriverConfig webDriverConfig = WebDriverConfigExecutor.loadConfigFromFile();
        ProxyConfigHolder proxyConfigHolder = new ProxySourcesClientLoader().getProxy();

        // Set WebDriver executable
        //System.setProperty("webdriver.chrome.driver", Objects.requireNonNull(WebDriverInitializer.class.getClassLoader().getResource(webDriverConfig.getWebDriverExecutable())).getPath());
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\" + webDriverConfig.getWebDriverExecutable());
        // Configure Chrome options
        ChromeOptions options = new ChromeOptions();

        // Set user agent if provided
        if (webDriverConfig.getUserAgent() != null) {
            options.addArguments("--user-agent=" + webDriverConfig.getUserAgent());
        }

        // Create proxy file config initializer
        ProxyFileConfigInitializer proxyFileConfigInitializer = new ProxyFileConfigInitializer();

        // Set proxy if provided
        if (proxyConfigHolder != null && proxyConfigHolder.getProxyNetworkConfig() != null) {
            //Proxy proxy = getProxy(proxyConfigHolder);

            // Alternative method to create proxy
            // without credentials
            //options.addArguments("--proxy-server=51.81.31.169:4485");

            // Create zip file via provided
            // proxy settings
            File file = null;
            try {
                file = proxyFileConfigInitializer.initProxyConfigFile(
                        proxyConfigHolder.getProxyNetworkConfig().getHostname(),
                        proxyConfigHolder.getProxyNetworkConfig().getPort(),
                        proxyConfigHolder.getProxyCredentials().getUsername(),
                        proxyConfigHolder.getProxyCredentials().getPassword()
                );

            } catch (IOException e) {
                e.printStackTrace();
            }

            // add extension containing
            // all proxy configs via a zip file
            // in proxy/temp/<counter>/
            options.addExtensions(file);

            //options.setProxy(proxy);

            LOGGER.log(Level.INFO, String.format("Proxy configured: %s:%d", proxyConfigHolder.getProxyNetworkConfig().getHostname(), proxyConfigHolder.getProxyNetworkConfig().getPort()));
        }

        // Initialize ChromeDriver
        ChromeDriver driver = new ChromeDriver(options);

        // Free space from hard-drive after
        // chrome driver has been initialized
        proxyFileConfigInitializer.clearDirectory();

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
        proxy.setHttpProxy(proxyAddressAndAuth);
        proxy.setSslProxy(proxyAddressAndAuth);

        LOGGER.log(Level.INFO, "Proxy configured successfully: " + proxyAddressAndAuth);
        return proxy;
    }
}

package executor.service.webdriver;

import executor.service.config.ChromeProxyConfigurer;
import executor.service.config.proxy.ProxySourcesClient;
import executor.service.model.ProxyConfigHolder;
import executor.service.model.WebDriverConfig;
import executor.service.config.PropertiesConfigHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.time.Duration;


public class ChromeDriverInitializer implements WebDriverInitializer {

    private static final Logger LOGGER = LogManager.getLogger(ChromeDriverInitializer.class.getName());

    private final ChromeProxyConfigurer chromeProxyConfigurer;
    private final ProxySourcesClient proxySourcesClient;

    public ChromeDriverInitializer(ChromeProxyConfigurer chromeProxyConfigurer,
                                   ProxySourcesClient proxySourcesClient) {

        this.chromeProxyConfigurer = chromeProxyConfigurer;
        this.proxySourcesClient = proxySourcesClient;
    }

    @Override
    public WebDriver init() {
        LOGGER.info("Initializing WebDriver...");

        WebDriverConfig webDriverConfig = PropertiesConfigHolder.loadConfigFromFile();
        ProxyConfigHolder proxyConfigHolder = proxySourcesClient.getProxy();

        // Set WebDriver executable
        System.setProperty("webdriver.chrome.driver", webDriverConfig.getWebDriverExecutable());

        // Configure Chrome options
        ChromeOptions options = new ChromeOptions();

        // Set user agent if provided
        if (webDriverConfig.getUserAgent() != null) {
            options.addArguments("--user-agent=" + webDriverConfig.getUserAgent());
        }

        // Set a callback function to clear memory after use
        // initially set to do nothing
        Runnable clearMemory = () -> {};
        // Set proxy with credentials, set proxy or skip
        if (proxyConfigHolder != null) {

            if (proxyHasNetwork(proxyConfigHolder) && proxyHasCredentials(proxyConfigHolder)) {

                try {
                    clearMemory = chromeProxyConfigurer.configureProxy(options, proxyConfigHolder);
                } catch (IOException e) {
                    LOGGER.error(e);
                }

            } else if (proxyHasNetwork(proxyConfigHolder)) {

                options.addArguments(String.format("--proxy-server=%s:%s",
                        proxyConfigHolder.getProxyNetworkConfig().getHostname(),
                        proxyConfigHolder.getProxyNetworkConfig().getPort()
                ));

            }
        }

        // Initialize ChromeDriver
        ChromeDriver driver = new ChromeDriver(options);

        // Clear memory after driver has been initialized
        if (clearMemory != null)
            clearMemory.run();

        // Configure timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(webDriverConfig.getImplicitlyWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofMillis(webDriverConfig.getPageLoadTimeout()));

        LOGGER.info("WebDriver initialized successfully.");
        return driver;
    }

    private boolean proxyHasNetwork(ProxyConfigHolder proxyConfigHolder) {

        if (proxyConfigHolder == null)
            return false;

        if (proxyConfigHolder.getProxyNetworkConfig() == null)
            return false;

        if (proxyConfigHolder.getProxyNetworkConfig().getHostname() == null
                || proxyConfigHolder.getProxyNetworkConfig().getPort() == null)
            return false;

        return true;
    }

    private boolean proxyHasCredentials(ProxyConfigHolder proxyConfigHolder) {

        if (proxyConfigHolder == null)
            return false;

        if (proxyConfigHolder.getProxyCredentials() == null)
            return false;

        if (proxyConfigHolder.getProxyCredentials().getUsername() == null
                || proxyConfigHolder.getProxyCredentials().getPassword() == null)
            return false;

        return true;
    }
}
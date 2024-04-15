package executor.service.webdriver;

import executor.service.config.ChromeProxyConfigurer;
import executor.service.config.PropertiesConfigHolder;
import executor.service.config.proxy.ProxySourcesClient;
import executor.service.model.ProxyConfigHolder;
import executor.service.model.WebDriverConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;

@Service
public class ChromeDriverInitializer implements WebDriverInitializer {

    private static final Logger LOGGER = LogManager.getLogger(ChromeDriverInitializer.class.getName());
    private static final String WEBDRIVER_PROPERTY_NAME = "webdriver.chrome.driver";
    private final ChromeProxyConfigurer chromeProxyConfigurer;
    private final ProxySourcesClient proxySourcesClient;
    private final PropertiesConfigHolder propertiesConfigHolder;

    public ChromeDriverInitializer(ChromeProxyConfigurer chromeProxyConfigurer,
                                   @Qualifier("ProxySourcesClientOkHttp") ProxySourcesClient proxySourcesClient,
                                   PropertiesConfigHolder propertiesConfigHolder) {
        this.chromeProxyConfigurer = chromeProxyConfigurer;
        this.proxySourcesClient = proxySourcesClient;
        this.propertiesConfigHolder = propertiesConfigHolder;
    }

    @Override
    public WebDriver init() {
        LOGGER.info("Starting WebDriver configuration...");

        WebDriverConfig webDriverConfig = propertiesConfigHolder.getWebDriverConfig();
        ChromeOptions options = configureChromeOptions(webDriverConfig);

        ProxyConfigHolder proxyConfigHolder = proxySourcesClient.getProxy();
        Runnable clearMemory = () -> {
        };
        if (proxyConfigHolder != null) {
            LOGGER.info("Configuring proxy...");
            clearMemory = configureProxy(options, proxyConfigHolder);
        }

        ChromeDriver driver = null;
        try {
            LOGGER.info("Initializing WebDriver...");
            driver = createChromeDriver(options);
            configureTimeouts(driver, webDriverConfig);

        } catch (Exception e) {
            LOGGER.error("WebDriver initialization failed");
            throw e;

        } finally {
            // Clear memory after driver has been initialized
            if (clearMemory != null)
                clearMemory.run();
        }

        LOGGER.info("WebDriver initialized successfully.");
        return driver;
    }

    private ChromeOptions configureChromeOptions(WebDriverConfig webDriverConfig) {
        ChromeOptions options = new ChromeOptions();
        System.setProperty(WEBDRIVER_PROPERTY_NAME, webDriverConfig.getWebDriverExecutable());

        if (webDriverConfig.getUserAgent() != null) {
            options.addArguments("--user-agent=" + webDriverConfig.getUserAgent());
        }
        return options;
    }

    private Runnable configureProxy(ChromeOptions options, ProxyConfigHolder proxyConfigHolder) {
        if (proxyConfigHolder == null) {
            return null;
        }

        if (proxyHasNetwork(proxyConfigHolder)) {
            LOGGER.info("Using proxy: \"{}:{}\"", proxyConfigHolder.getProxyNetworkConfig().getHostname(),
                    proxyConfigHolder.getProxyNetworkConfig().getPort());

            if (proxyHasCredentials(proxyConfigHolder)) {
                try {
                    return chromeProxyConfigurer.configureProxy(options, proxyConfigHolder);
                } catch (IOException e) {
                    LOGGER.error("Error configuring proxy: {}", e.getMessage());
                }
            } else {
                options.addArguments(String.format("--proxy-server=%s:%s",
                        proxyConfigHolder.getProxyNetworkConfig().getHostname(),
                        proxyConfigHolder.getProxyNetworkConfig().getPort()));
            }
        }

        return null;
    }


    private ChromeDriver createChromeDriver(ChromeOptions options) {
        return new ChromeDriver(options);
    }

    private void configureTimeouts(WebDriver driver, WebDriverConfig webDriverConfig) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(webDriverConfig.getImplicitlyWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofMillis(webDriverConfig.getPageLoadTimeout()));
    }

    private boolean proxyHasNetwork(ProxyConfigHolder proxyConfigHolder) {
        return proxyConfigHolder != null &&
                proxyConfigHolder.getProxyNetworkConfig() != null &&
                proxyConfigHolder.getProxyNetworkConfig().getHostname() != null &&
                proxyConfigHolder.getProxyNetworkConfig().getPort() != null;
    }

    private boolean proxyHasCredentials(ProxyConfigHolder proxyConfigHolder) {
        return proxyConfigHolder != null &&
                proxyConfigHolder.getProxyCredentials() != null &&
                proxyConfigHolder.getProxyCredentials().getUsername() != null &&
                proxyConfigHolder.getProxyCredentials().getPassword() != null;
    }
}
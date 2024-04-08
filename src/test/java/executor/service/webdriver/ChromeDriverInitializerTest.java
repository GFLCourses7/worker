package executor.service.webdriver;

import executor.service.config.ChromeProxyConfigurer;
import executor.service.config.PropertiesConfigHolder;
import executor.service.config.proxy.ProxySourcesClient;
import executor.service.model.ProxyConfigHolder;
import executor.service.model.ProxyCredentials;
import executor.service.model.ProxyNetworkConfig;
import executor.service.model.WebDriverConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class ChromeDriverInitializerTest {

    @Mock
    private ChromeProxyConfigurer chromeProxyConfigurer;

    @Mock
    private ProxySourcesClient proxySourcesClient;

    @Mock
    private PropertiesConfigHolder propertiesConfigHolder;

    private WebDriverConfig webDriverConfig;
    private WebDriver driver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        webDriverConfig = new WebDriverConfig("./chromedriver",
                "userAgent",
                3L, 10L);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void testInit() {
        // Mock data
        ProxyConfigHolder proxyConfigHolder = new ProxyConfigHolder(new ProxyNetworkConfig("Test proxy", 9090), new ProxyCredentials());

        // Mock behavior
        when(propertiesConfigHolder.getWebDriverConfig()).thenReturn(webDriverConfig);
        when(proxySourcesClient.getProxy()).thenReturn(proxyConfigHolder);


        // Initialize ChromeDriverInitializer
        ChromeDriverInitializer initializer = new ChromeDriverInitializer(chromeProxyConfigurer, proxySourcesClient, propertiesConfigHolder);
        driver = initializer.init();

        // Assertions
        assertNotNull(driver);
        assertInstanceOf(ChromeDriver.class, driver);
    }
}

package executor.service.webdriver;

import executor.service.model.ProxyConfigHolder;
import executor.service.model.ProxyNetworkConfig;
import executor.service.model.WebDriverConfig;
import executor.service.config.PropertiesConfigHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class ChromeDriverInitializerTest {

    private WebDriverConfig webDriverConfig;

    @BeforeEach
    public void setUp() {
        webDriverConfig = PropertiesConfigHolder.loadConfigFromFile();
    }

    @Test
    public void testInitWebDriverWithProxy() {
        try (MockedStatic<PropertiesConfigHolder> fakePropertiesConfigHolder = Mockito.mockStatic(PropertiesConfigHolder.class)) {
            fakePropertiesConfigHolder.when(PropertiesConfigHolder::loadConfigFromFile)
                    .thenReturn(webDriverConfig);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ProxyNetworkConfig proxyNetworkConfig = new ProxyNetworkConfig();
        proxyNetworkConfig.setHostname("proxy.host");
        proxyNetworkConfig.setPort(8080);

        ProxyConfigHolder proxyConfigHolder = new ProxyConfigHolder();
        proxyConfigHolder.setProxyNetworkConfig(proxyNetworkConfig);

        ChromeDriverInitializer chromeDriverInitializer = Mockito.spy(new ChromeDriverInitializer());
        when(chromeDriverInitializer.loadWebDriverConfig()).thenReturn(webDriverConfig);
        when(chromeDriverInitializer.loadProxyConfig()).thenReturn(proxyConfigHolder);

        ChromeOptions options = new ChromeOptions();
        Proxy proxy = mock(Proxy.class);
        options.setCapability(CapabilityType.PROXY, proxy);
        when(chromeDriverInitializer.configureChromeOptions(webDriverConfig)).thenReturn(options);

        // Act
        WebDriver driver = chromeDriverInitializer.init();

        // Assert
        assertNotNull(driver);
        verify(chromeDriverInitializer, times(1)).loadWebDriverConfig();
        verify(chromeDriverInitializer, times(1)).loadProxyConfig();
        verify(chromeDriverInitializer, times(1)).configureChromeOptions(webDriverConfig);
        verify(chromeDriverInitializer, times(1)).createChromeDriver(options);

        driver.quit();

    }

    @Test
    public void testInitWebDriverWithoutProxy() {

        try (MockedStatic<PropertiesConfigHolder> fakePropertiesConfigHolder = Mockito.mockStatic(PropertiesConfigHolder.class)) {
            fakePropertiesConfigHolder.when(PropertiesConfigHolder::loadConfigFromFile)
                    .thenReturn(webDriverConfig);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ChromeDriverInitializer chromeDriverInitializer = Mockito.spy(new ChromeDriverInitializer());
        when(chromeDriverInitializer.loadWebDriverConfig()).thenReturn(webDriverConfig);
        when(chromeDriverInitializer.loadProxyConfig()).thenReturn(null);

        // Act
        WebDriver driver = chromeDriverInitializer.init();

        // Assert
        assertNotNull(driver);
        verify(chromeDriverInitializer, times(1)).loadWebDriverConfig();
        verify(chromeDriverInitializer, times(1)).loadProxyConfig();
        verify(chromeDriverInitializer, times(1)).configureChromeOptions(any());
        verify(chromeDriverInitializer, times(1)).createChromeDriver(any());

        driver.quit();
    }
}

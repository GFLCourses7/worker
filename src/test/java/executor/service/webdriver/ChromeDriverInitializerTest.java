package executor.service.webdriver;

import executor.service.config.ChromeProxyConfigurerAddon;
import executor.service.config.ChromeProxyConfigurerBrowserMob;
import executor.service.config.proxy.ProxySourcesClientLoader;
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


    /*
     *  !!! IMPORTANT !!!
     *
     *  net.lightbody.bmp library has broken digital signature .sb file
     *  hence creating a .jar file with it is impossible unless .sb
     *  files are deleted. For now it has been removed from the .pom
     *  file
     *
     * */

    public void testInitWebDriverWithProxy() {

        // Uses net.lightbody.bmp for testing, requires reconfiguration

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

        ChromeDriverInitializer chromeDriverInitializer = Mockito.spy(new ChromeDriverInitializer(new ChromeProxyConfigurerBrowserMob(), new ProxySourcesClientLoader()));
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

        ChromeDriverInitializer chromeDriverInitializer = Mockito.spy(new ChromeDriverInitializer(new ChromeProxyConfigurerBrowserMob(), new ProxySourcesClientLoader()));
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

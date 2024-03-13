package executor.service.webdriver;

import executor.service.model.ProxyConfigHolder;
import executor.service.model.ProxyCredentials;
import executor.service.model.ProxyNetworkConfig;
import executor.service.config.JsonConfigReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.openqa.selenium.WebDriver;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;

public class WebDriverInitializerTest {
    private ProxyNetworkConfig fakeProxyNetworkConfig;
    private ProxyCredentials fakeProxyCredentials;
    private ProxyConfigHolder fakeProxyConfigHolder;

    @BeforeEach
    void setUp() {
        fakeProxyNetworkConfig = new ProxyNetworkConfig("23.27.3.31", 50100);
        fakeProxyCredentials = new ProxyCredentials("denyslviv2013", "REtsLkZs2I");
        fakeProxyConfigHolder = new ProxyConfigHolder(fakeProxyNetworkConfig, fakeProxyCredentials);
    }

    @Test
    public void testSuccessInit() {
        List<ProxyConfigHolder> fakeProxyConfigHolderList = new ArrayList<>();
        fakeProxyConfigHolderList.add(fakeProxyConfigHolder);

        try (MockedStatic<JsonConfigReader> utilities = mockStatic(JsonConfigReader.class)) {
            utilities.when(() -> JsonConfigReader.readFile(anyString(), eq(ProxyConfigHolder.class)))
                    .thenReturn(fakeProxyConfigHolderList);

            ChromeDriverInitializer webDriverInitializer = new ChromeDriverInitializer();

            WebDriver init = webDriverInitializer.init();
            init.get("https://2ip.io/");
            Thread.sleep(5000); // 5s
            init.quit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testErrorInit() {
        List<ProxyConfigHolder> fakeProxyConfigHolderList = new ArrayList<>();

        try (MockedStatic<JsonConfigReader> utilities = mockStatic(JsonConfigReader.class)) {
            utilities.when(() -> JsonConfigReader.readFile(anyString(), eq(ProxyConfigHolder.class)))
                    .thenReturn(fakeProxyConfigHolderList);

            ChromeDriverInitializer webDriverInitializer = new ChromeDriverInitializer();
            Assertions.assertThrows(NoSuchElementException.class, webDriverInitializer::init);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

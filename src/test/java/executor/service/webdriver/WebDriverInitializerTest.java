package executor.service.webdriver;

import executor.service.config.ChromeProxyConfigurerAddon;
import executor.service.config.ProxyConfigFileInitializer;
import executor.service.config.proxy.ProxySourcesClientLoader;
import executor.service.model.ProxyConfigHolder;
import executor.service.model.ProxyCredentials;
import executor.service.model.ProxyNetworkConfig;
import executor.service.config.JsonConfigReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.openqa.selenium.WebDriver;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class WebDriverInitializerTest {
    private ProxyNetworkConfig fakeProxyNetworkConfig;
    private ProxyCredentials fakeProxyCredentials;
    private ProxyConfigHolder fakeProxyConfigHolder;

    @BeforeEach
    void setUp() {
        fakeProxyNetworkConfig = new ProxyNetworkConfig("193.41.68.93", 50100);
        fakeProxyCredentials = new ProxyCredentials("denyslviv2013", "REtsLkZs2I");
        fakeProxyConfigHolder = new ProxyConfigHolder(fakeProxyNetworkConfig, fakeProxyCredentials);
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

    @Test
    public void testSuccessInit() {
        List<ProxyConfigHolder> fakeProxyConfigHolderList = new ArrayList<>();
        fakeProxyConfigHolderList.add(fakeProxyConfigHolder);

        try (MockedStatic<JsonConfigReader> utilities = mockStatic(JsonConfigReader.class)) {
            utilities.when(() -> JsonConfigReader.readFile(any(byte[].class), eq(ProxyConfigHolder.class)))
                    .thenReturn(fakeProxyConfigHolderList);

            ChromeDriverInitializer webDriverInitializer
                    = new ChromeDriverInitializer(new ChromeProxyConfigurerAddon(ProxyConfigFileInitializer::new), new ProxySourcesClientLoader());

            WebDriver init = webDriverInitializer.init();
            init.get("https://2ip.io/");
            Thread.sleep(5000); // 5s
            init.quit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void testErrorInit() {

        // Proxy behaviour has been changed, needs reconfiguration

//        List<ProxyConfigHolder> fakeProxyConfigHolderList = new ArrayList<>();
//
//        try (MockedStatic<JsonConfigReader> utilities = mockStatic(JsonConfigReader.class)) {
//            utilities.when(() -> JsonConfigReader.readFile(anyString(), eq(ProxyConfigHolder.class)))
//                    .thenReturn(fakeProxyConfigHolderList);
//
//            ChromeDriverInitializer webDriverInitializer
//                    = new ChromeDriverInitializer(new ChromeProxyConfigurerBrowserMob(), new ProxySourcesClientLoader());
//            Assertions.assertThrows(NoSuchElementException.class, webDriverInitializer::init);
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }
}

package executor.service.webdriver;

import executor.service.model.ProxyCredentials;
import executor.service.model.ProxyNetworkConfig;
import executor.service.utils.JsonConfigReader;
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

    @BeforeEach
    void setUp() {
        fakeProxyNetworkConfig = new ProxyNetworkConfig("35.185.196.38", 3128);
        fakeProxyCredentials = null;
    }

    @Test
    public void testSuccessInit() {
        List<ProxyNetworkConfig> fakeProxyNetworkConfigList = new ArrayList<>();
        fakeProxyNetworkConfigList.add(fakeProxyNetworkConfig);
        List<ProxyCredentials> fakeProxyCredentialsList = new ArrayList<>();
        fakeProxyCredentialsList.add(fakeProxyCredentials);

        try (MockedStatic<JsonConfigReader> utilities = mockStatic(JsonConfigReader.class)) {
            utilities.when(() -> JsonConfigReader.readFile(eq("ProxyNetwork.json"), any()))
                    .thenReturn(fakeProxyNetworkConfigList);
            utilities.when(() -> JsonConfigReader.readFile(eq("ProxyCredentials.json"), any()))
                    .thenReturn(fakeProxyCredentialsList);

            ChromeDriverInitializer webDriverInitializer = new ChromeDriverInitializer();

            WebDriver init = webDriverInitializer.init();

            init.get("https://2ip.io/");

            Thread.sleep(10000); // 10s

            init.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

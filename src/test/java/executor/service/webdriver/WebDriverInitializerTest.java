package executor.service.webdriver;

import executor.service.model.ProxyCredentials;
import executor.service.model.ProxyNetworkConfig;
import executor.service.utils.JsonConfigReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;

public class WebDriverInitializerTest {
    private ProxyNetworkConfig fakeProxyNetworkConfig;
    private ProxyCredentials fakeProxyCredentials;

    @BeforeEach
    void setUp() {
        fakeProxyNetworkConfig = new ProxyNetworkConfig("localhost", 8080);
        fakeProxyCredentials = new ProxyCredentials("login", "password");
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

            WebDriverInitializerImpl webDriverInitializer = new WebDriverInitializerImpl();
            webDriverInitializer.init().close();
        }
    }
}

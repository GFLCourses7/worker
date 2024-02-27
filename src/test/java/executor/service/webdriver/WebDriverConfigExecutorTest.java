package executor.service.webdriver;

import executor.service.model.WebDriverConfig;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WebDriverConfigExecutorTest {

    @Test
    void testLoadConfigFromFile() {
        WebDriverConfig webDriverConfig = WebDriverConfigExecutor.loadConfigFromFile();

        assertNotNull(webDriverConfig);
        assertNotNull(webDriverConfig.getWebDriverExecutable());
        assertNotNull(webDriverConfig.getUserAgent());
        assertNotEquals(0, webDriverConfig.getPageLoadTimeout());
        assertNotEquals(0, webDriverConfig.getImplicitlyWait());
    }
}

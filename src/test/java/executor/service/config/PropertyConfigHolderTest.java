package executor.service.config;

import executor.service.model.ThreadPoolConfig;
import executor.service.model.WebDriverConfig;
import executor.service.config.PropertiesConfigHolder;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PropertyConfigHolderTest {

    @Test
    void testLoadConfigFromFile() {
        WebDriverConfig webDriverConfig = PropertiesConfigHolder.loadConfigFromFile();

        assertNotNull(webDriverConfig);
        assertNotNull(webDriverConfig.getWebDriverExecutable());
        assertNotNull(webDriverConfig.getUserAgent());
        assertNotEquals(0, webDriverConfig.getPageLoadTimeout());
        assertNotEquals(0, webDriverConfig.getImplicitlyWait());
    }

    @Test
    public void testLoadThreadConfigFromFile() {
        ThreadPoolConfig threadPoolConfig = PropertiesConfigHolder.loadThreadConfigFromFile();

        assertNotNull(threadPoolConfig);
        assertNotEquals(0, threadPoolConfig.getCorePoolSize());
        assertNotEquals(0, threadPoolConfig.getKeepAliveTime());
    }

    @Test
    public void loadMaxPoolSizeFromFile() {
        int maxPoolSize = PropertiesConfigHolder.loadMaxPoolSizeFromFile();

        assertNotEquals(0, maxPoolSize);

    }
}

package executor.service.config;

import executor.service.model.ThreadPoolConfig;
import executor.service.model.WebDriverConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class PropertiesConfigHolderTest {

    @Autowired
    private PropertiesConfigHolder configHolder;

    @Test
    void testGetWebDriverConfig() {
        WebDriverConfig webDriverConfig = configHolder.getWebDriverConfig();

        assertNotNull(webDriverConfig);
        assertNotNull(webDriverConfig.getWebDriverExecutable());
        assertNotNull(webDriverConfig.getUserAgent());
        assertNotEquals(0, webDriverConfig.getPageLoadTimeout());
        assertNotEquals(0, webDriverConfig.getImplicitlyWait());
    }

    @Test
    public void testGetThreadPoolConfig() {
        ThreadPoolConfig threadPoolConfig = configHolder.getThreadPoolConfig();

        assertNotNull(threadPoolConfig);
        assertNotEquals(0, threadPoolConfig.getCorePoolSize());
        assertNotEquals(0, threadPoolConfig.getKeepAliveTime());
    }

    @Test
    public void testGetMaxPoolSize() {
        int maxPoolSize = configHolder.getMaxPoolSize();

        assertNotEquals(0, maxPoolSize);
    }
}
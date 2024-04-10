package executor.service.config;

import executor.service.model.ThreadPoolConfig;
import executor.service.model.WebDriverConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class PropertiesConfigHolderTest {

    @Mock
    private PropertiesConfigHolder configHolder;

    @Value("${executorservice.common.webDriverExecutable}")
    private String webDriverExecutable;

    @Value("${executorservice.common.userAgent}")
    private String userAgent;

    @Value("${executorservice.common.pageLoadTimeout}")
    private long pageLoadTimeout;

    @Value("${executorservice.common.driverWait}")
    private long driverWait;

    @Value("${executorservice.thread.corePoolSize}")
    private int corePoolSize;

    @Value("${executorservice.thread.keepAliveTime}")
    private long keepAliveTime;

    @Value("${executorservice.thread.maxPoolSize}")
    private int maxPoolSize;

    public PropertiesConfigHolderTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetWebDriverConfig() {
        when(configHolder.getWebDriverConfig()).thenReturn(new WebDriverConfig(webDriverExecutable, userAgent, pageLoadTimeout, driverWait));
        WebDriverConfig expectedConfig = new WebDriverConfig(webDriverExecutable, userAgent, pageLoadTimeout, driverWait);
        assertEquals(expectedConfig, configHolder.getWebDriverConfig());
    }

    @Test
    public void testGetThreadPoolConfig() {
        when(configHolder.getThreadPoolConfig()).thenReturn(new ThreadPoolConfig(corePoolSize, keepAliveTime));
        ThreadPoolConfig expectedConfig = new ThreadPoolConfig(corePoolSize, keepAliveTime);
        assertEquals(expectedConfig, configHolder.getThreadPoolConfig());
    }

    @Test
    public void testGetMaxPoolSize() {
        when(configHolder.getMaxPoolSize()).thenReturn(maxPoolSize);
        assertEquals(maxPoolSize, configHolder.getMaxPoolSize());
    }
}

package executor.service.executionService;

import executor.service.model.ThreadPoolConfig;
import executor.service.scenario.ScenarioSourceListenerImpl;
import executor.service.executor.ExecutionService;
import executor.service.scenario.ScenarioExecutor;
import executor.service.webdriver.WebDriverInitializer;
import org.apache.commons.configuration2.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.WebDriver;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ParallelFlowExecutorServiceTest {

    @Mock
    private Configuration mockConfiguration;

    @Mock
    private ScenarioSourceListenerImpl mockScenarioSourceListener;

    @Mock
    private ExecutionService mockExecutionService;

    @Mock
    private WebDriverInitializer mockWebDriverInitializer;

    @Mock
    private ScenarioExecutor mockScenarioExecutor;

    @InjectMocks
    private ParallelFlowExecutorService parallelFlowExecutorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(mockConfiguration.getInt("executorservice.common.threadsCount")).thenReturn(1);
        when(mockConfiguration.getLong("executorservice.common.pageLoadTimeout")).thenReturn(30000L);

        when(mockWebDriverInitializer.init()).thenReturn(mock(WebDriver.class));

        parallelFlowExecutorService = new ParallelFlowExecutorService(
                mockScenarioSourceListener,
                mockExecutionService,
                mockWebDriverInitializer,
                mockScenarioExecutor);
    }
    @Test
    public void testInit() {
        ThreadPoolConfig threadPoolConfig = parallelFlowExecutorService.init();
        assertNotNull(threadPoolConfig);
        assertEquals(1, threadPoolConfig.getCorePoolSize());
        assertEquals(30000L, threadPoolConfig.getKeepAliveTime());
    }
}

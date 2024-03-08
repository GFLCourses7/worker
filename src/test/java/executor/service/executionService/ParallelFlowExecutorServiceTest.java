package executor.service.executionService;

import executor.service.model.Scenario;
import executor.service.model.ThreadPoolConfig;
import executor.service.scenario.ScenarioSourceListenerImpl;
import executor.service.executor.ExecutionService;
import executor.service.scenario.ScenarioExecutor;
import executor.service.utils.PropertiesConfigHolder;
import executor.service.webdriver.WebDriverInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ParallelFlowExecutorServiceTest {

    @Mock
    private ScenarioSourceListenerImpl mockScenarioSourceListener;
    @Mock
    private ExecutionService mockExecutionService;
    @Mock
    private WebDriverInitializer mockWebDriverInitializer;
    @Mock
    private WebDriver mockWebDriver;
    @Mock
    private ScenarioExecutor mockScenarioExecutor;
    @Spy
    private ThreadPoolConfig threadPoolConfig = new ThreadPoolConfig(3, 20000L);
    private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
    @Mock
    private ThreadPoolExecutor mockThreadPoolExecutor;
    @InjectMocks
    private ParallelFlowExecutorService parallelFlowExecutorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

       mockThreadPoolExecutor = new ThreadPoolExecutor(threadPoolConfig.getCorePoolSize(), 10,
                threadPoolConfig.getKeepAliveTime(), TimeUnit.MILLISECONDS, queue);

        when(mockWebDriverInitializer.init()).thenReturn(mockWebDriver);

        parallelFlowExecutorService = new ParallelFlowExecutorService(
                mockScenarioSourceListener,
                mockExecutionService,
                mockWebDriverInitializer,
                mockScenarioExecutor);
    }
    @Test
    public void initThreadPoolConfigTest() {
        ThreadPoolConfig threadPoolConfig = PropertiesConfigHolder.initThreadConfig();
        assertNotNull(threadPoolConfig);
        assertEquals(2, threadPoolConfig.getCorePoolSize());
        assertEquals(30000L, threadPoolConfig.getKeepAliveTime());
    }
    @Test
    public void startThreadsShouldNotExecuteThreadsWhenListEmptyTest() {

        when(mockScenarioSourceListener.getScenarios()).thenReturn(Collections.emptyList());

        parallelFlowExecutorService.setThreadPoolExecutor(mockThreadPoolExecutor);
        parallelFlowExecutorService.startThreads();

        assertEquals(0, parallelFlowExecutorService.getThreadPoolExecutor().getActiveCount());
        assertTrue(parallelFlowExecutorService.getThreadPoolExecutor().isShutdown());
    }

    @Test
    public void executeRunnableScenarioInParallelTest() {
        ScenarioSourceListenerImpl scenarioSourceListener = new ScenarioSourceListenerImpl();
        Scenario scenario = new Scenario();

        List<Scenario> scenarioList = new ArrayList<>(Arrays.asList(scenario,scenario,scenario));

        scenarioSourceListener.setScenarios(scenarioList);

        ParallelFlowExecutorService parallelFlowExecutorService = new ParallelFlowExecutorService(
                scenarioSourceListener,mockExecutionService,
                mockWebDriverInitializer,
                mockScenarioExecutor
        );
        parallelFlowExecutorService.setThreadPoolExecutor(mockThreadPoolExecutor);
        parallelFlowExecutorService.startThreads();

        assertEquals(3, parallelFlowExecutorService.getThreadPoolExecutor().getActiveCount());
        assertTrue(parallelFlowExecutorService.getThreadPoolExecutor().isShutdown());
    }
}

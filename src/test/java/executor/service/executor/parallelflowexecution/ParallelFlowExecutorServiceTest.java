package executor.service.executor.parallelflowexecution;

import executor.service.executor.parallelflowexecution.ParallelFlowExecutorService;
import executor.service.model.Scenario;
import executor.service.model.ThreadPoolConfig;
import executor.service.listener.ScenarioSourceListenerImpl;
import executor.service.executor.executionservice.ExecutionServiceImpl;
import executor.service.executor.scenarioexecutor.ScenarioExecutor;
import executor.service.config.PropertiesConfigHolder;
import executor.service.webdriver.WebDriverInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.openqa.selenium.WebDriver;

import java.util.*;
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
    private ExecutionServiceImpl mockExecutionService;
    @Mock
    private WebDriverInitializer mockWebDriverInitializer;
    @Mock
    private WebDriver mockWebDriver;
    @Mock
    private ScenarioExecutor mockScenarioExecutor;
    @Spy
    private ThreadPoolConfig threadPoolConfig = new ThreadPoolConfig(4, 20000L);
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
    public void testStartThreadsShouldNotStartThreadsWhenQueueEmpty() {

        when(mockScenarioSourceListener.getScenarios()).thenReturn(null);

        parallelFlowExecutorService.setThreadPoolExecutor(mockThreadPoolExecutor);
        parallelFlowExecutorService.startThreads();

        assertEquals(0, parallelFlowExecutorService.getThreadPoolExecutor().getActiveCount());
        assertFalse(parallelFlowExecutorService.getThreadPoolExecutor().isShutdown());
    }

    @Test
    public void testExecuteRunnableScenarioInParallel() {
        ScenarioSourceListenerImpl scenarioSourceListener = new ScenarioSourceListenerImpl();
        Scenario scenario = new Scenario();

        LinkedBlockingQueue<Scenario> scenarioQueue = new LinkedBlockingQueue<>(Arrays.asList(scenario,scenario,scenario));

        scenarioSourceListener.setScenarios(scenarioQueue);

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

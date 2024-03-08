package executor.service.executionService;

import executor.service.executor.ExecutionService;
import executor.service.model.ThreadPoolConfig;
import executor.service.scenario.ScenarioExecutor;
import executor.service.scenario.ScenarioSourceListenerImpl;
import executor.service.utils.PropertiesConfigHolder;
import executor.service.webdriver.WebDriverInitializer;

import org.openqa.selenium.WebDriver;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParallelFlowExecutorService {

    private final ThreadPoolConfig threadPoolConfig;
    private ThreadPoolExecutor threadPoolExecutor;
    private final ScenarioSourceListenerImpl scenarioSourceListener;
    private final ExecutionService executionService;
    private final WebDriver webDriver;
    private final ScenarioExecutor scenarioExecutor;
    private static final int MAX_POOL_SIZE = 10;
    private static final Logger LOGGER = LogManager.getLogger(ParallelFlowExecutorService.class.getName());

    public ParallelFlowExecutorService(ScenarioSourceListenerImpl scenarioSourceListener,
                                       ExecutionService executionService,
                                       WebDriverInitializer webDriverInitializer,
                                       ScenarioExecutor scenarioExecutor
    ) {
        this.scenarioSourceListener = scenarioSourceListener;
        this.executionService = executionService;
        this.webDriver = webDriverInitializer.init();
        this.scenarioExecutor = scenarioExecutor;
        this.threadPoolConfig = PropertiesConfigHolder.initThreadConfig();
        this.threadPoolExecutor = initExecutor();
    }

    ThreadPoolExecutor initExecutor() {
        LinkedBlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>();
        ThreadPoolExecutor newThreadPoolExecutor = new ThreadPoolExecutor(threadPoolConfig.getCorePoolSize(),
                MAX_POOL_SIZE,
                threadPoolConfig.getKeepAliveTime(),
                TimeUnit.MILLISECONDS,
                blockingQueue);

        LOGGER.info("Create new ThreadPoolExecutor with parameters: " +
                "CorePoolSize - " + threadPoolConfig.getCorePoolSize() +
                " MaxPoolSize - " + MAX_POOL_SIZE +
                " KeepAliveTime - " + threadPoolConfig.getKeepAliveTime() +
                " milliseconds");
        return newThreadPoolExecutor;
    }

    public void startThreads() {

        int scenarioCount = scenarioSourceListener.getScenarios().size();

        for (int i = 0; i < scenarioCount; i++) {
            LOGGER.info("Start executing scenarios in threads");
            threadPoolExecutor.execute(() -> executionService.execute(webDriver, scenarioSourceListener, scenarioExecutor));
        }
        LOGGER.info("Shutdown ThreadPoolExecutor");
        threadPoolExecutor.shutdown();
    }
    public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }
    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }
}
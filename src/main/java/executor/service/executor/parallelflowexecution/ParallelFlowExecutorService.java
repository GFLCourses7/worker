package executor.service.executor.parallelflowexecution;

import executor.service.config.PropertiesConfigHolder;
import executor.service.executor.executionservice.ExecutionServiceImpl;
import executor.service.executor.scenarioexecutor.ScenarioExecutor;
import executor.service.listener.ScenarioSourceListener;
import executor.service.model.ThreadPoolConfig;
import executor.service.webdriver.WebDriverInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class ParallelFlowExecutorService {
    private static final Logger LOGGER = LogManager.getLogger(ParallelFlowExecutorService.class.getName());

    private ThreadPoolExecutor threadPoolExecutor;
    private final ScenarioSourceListener scenarioSourceListener;
    private final ExecutionServiceImpl executionService;
    private final WebDriverInitializer webDriverInitializer;
    private final ScenarioExecutor scenarioExecutor;
    private final PropertiesConfigHolder propertiesConfigHolder;

    public ParallelFlowExecutorService(ScenarioSourceListener scenarioSourceListener,
                                       ExecutionServiceImpl executionService,
                                       WebDriverInitializer webDriverInitializer,
                                       ScenarioExecutor scenarioExecutor,
                                       PropertiesConfigHolder propertiesConfigHolder
    ) {
        this.scenarioSourceListener = scenarioSourceListener;
        this.executionService = executionService;
        this.webDriverInitializer = webDriverInitializer;
        this.scenarioExecutor = scenarioExecutor;
        this.propertiesConfigHolder = propertiesConfigHolder;
        this.threadPoolExecutor = initExecutor();

        // Start threads after class initialization
        new Thread(this::startThreads).start();
    }

    ThreadPoolExecutor initExecutor() {
        LinkedBlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>();
        ThreadPoolConfig threadPoolConfig = propertiesConfigHolder.getThreadPoolConfig();
        ThreadPoolExecutor newThreadPoolExecutor = new ThreadPoolExecutor(threadPoolConfig.getCorePoolSize(),
                propertiesConfigHolder.getMaxPoolSize(),
                threadPoolConfig.getKeepAliveTime(),
                TimeUnit.MILLISECONDS,
                blockingQueue);

        LOGGER.info("Create new ThreadPoolExecutor with parameters: " +
                "CorePoolSize - " + threadPoolConfig.getCorePoolSize() +
                " MaxPoolSize - " + propertiesConfigHolder.getMaxPoolSize() +
                " KeepAliveTime - " + threadPoolConfig.getKeepAliveTime() +
                " milliseconds");
        return newThreadPoolExecutor;
    }

    public void startThreads() {

        LOGGER.info("Start executing scenarios in threads ");

        while (!Thread.interrupted()) {
            threadPoolExecutor.execute(() -> {
                // If executionService execute throws an error
                // try/catch will catch it and won't allow thread
                // to hang in an error state
                WebDriver webDriver = null;

                try {
                    webDriver = webDriverInitializer.init();
                    executionService.execute(webDriver,
                            scenarioSourceListener,
                            scenarioExecutor
                    );
                } catch (Exception e) {
                    LOGGER.error(e);
                } finally {
                    if (webDriver != null) {
                        webDriver.quit();
                    }
                }
            });

        }
        LOGGER.info("shutdown ThreadPoolExecutor");
        threadPoolExecutor.shutdown();
    }

    public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }
}
package executor.service.executor.parallelflowexecution;

import executor.service.executor.executionservice.ExecutionServiceImpl;
import executor.service.listener.ScenarioSourceListener;
import executor.service.model.Scenario;
import executor.service.model.ThreadPoolConfig;
import executor.service.executor.scenarioexecutor.ScenarioExecutor;
import executor.service.config.PropertiesConfigHolder;
import executor.service.webdriver.WebDriverInitializer;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ParallelFlowExecutorService {

    private final ThreadPoolConfig threadPoolConfig;
    private ThreadPoolExecutor threadPoolExecutor;
    private final ScenarioSourceListener scenarioSourceListener;
    private final ExecutionServiceImpl executionService;
    private final WebDriverInitializer webDriverInitializer;
    private final ScenarioExecutor scenarioExecutor;
    private static final Logger LOGGER = LogManager.getLogger(ParallelFlowExecutorService.class.getName());

    public ParallelFlowExecutorService(ScenarioSourceListener scenarioSourceListener,
                                       ExecutionServiceImpl executionService,
                                       WebDriverInitializer webDriverInitializer,
                                       ScenarioExecutor scenarioExecutor
    ) {
        this.scenarioSourceListener = scenarioSourceListener;
        this.executionService = executionService;
        this.webDriverInitializer = webDriverInitializer;
        this.scenarioExecutor = scenarioExecutor;
        this.threadPoolConfig = PropertiesConfigHolder.loadThreadConfigFromFile();
        this.threadPoolExecutor = initExecutor();

        // Start threads after class initialization
        new Thread(this::startThreads).start();
    }

    ThreadPoolExecutor initExecutor() {
        LinkedBlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>();
        ThreadPoolExecutor newThreadPoolExecutor = new ThreadPoolExecutor(threadPoolConfig.getCorePoolSize(),
                PropertiesConfigHolder.loadMaxPoolSizeFromFile(),
                threadPoolConfig.getKeepAliveTime(),
                TimeUnit.MILLISECONDS,
                blockingQueue);

        LOGGER.info("Create new ThreadPoolExecutor with parameters: " +
                "CorePoolSize - " + threadPoolConfig.getCorePoolSize() +
                " MaxPoolSize - " + PropertiesConfigHolder.loadMaxPoolSizeFromFile() +
                " KeepAliveTime - " + threadPoolConfig.getKeepAliveTime() +
                " milliseconds");
        return newThreadPoolExecutor;
    }

    public void startThreads() {

        LOGGER.info("Start executing scenarios in threads ");

        while (!Thread.interrupted()) {

            Scenario scenario = scenarioSourceListener.getScenario();

            threadPoolExecutor.execute(() -> {
                // If executionService execute throws an error
                // try/catch will catch it and won't allow thread
                // to hang in an error state
                try {
                    executionService.execute(webDriverInitializer.init(),
                            scenario,
                            scenarioExecutor
                    );
                } catch (Exception e) {
                    LOGGER.error(e);
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
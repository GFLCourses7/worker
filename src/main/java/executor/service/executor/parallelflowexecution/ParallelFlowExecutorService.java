package executor.service.executor.parallelflowexecution;

import executor.service.executor.executionservice.ExecutionServiceImpl;
import executor.service.executor.scenarioexecutor.ScenarioExecutor;
import executor.service.listener.ScenarioSourceListener;
import executor.service.webdriver.WebDriverInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class ParallelFlowExecutorService {
    private static final Logger LOGGER = LogManager.getLogger(ParallelFlowExecutorService.class.getName());

    private final ThreadPoolExecutor threadPoolExecutor;
    private final ScenarioSourceListener scenarioSourceListener;
    private final ExecutionServiceImpl executionService;
    private final WebDriverInitializer webDriverInitializer;
    private final ScenarioExecutor scenarioExecutor;

    public ParallelFlowExecutorService(ScenarioSourceListener scenarioSourceListener,
                                       ExecutionServiceImpl executionService,
                                       WebDriverInitializer webDriverInitializer,
                                       ScenarioExecutor scenarioExecutor,
                                       ThreadPoolExecutor threadPoolExecutor
    ) {
        this.scenarioSourceListener = scenarioSourceListener;
        this.executionService = executionService;
        this.webDriverInitializer = webDriverInitializer;
        this.scenarioExecutor = scenarioExecutor;
        this.threadPoolExecutor = threadPoolExecutor;

        // Start threads after class initialization
        new Thread(this::startThreads).start();
    }

    public void startThreads() {

        LOGGER.info("Start executing scenarios in threads ");

        while (!Thread.interrupted()) {

            while (!threadPoolExecutor.getQueue().isEmpty()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage());
                }
            }

            threadPoolExecutor.execute(() -> {
                try {
                    executionService.execute(webDriverInitializer,
                            scenarioSourceListener,
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
}
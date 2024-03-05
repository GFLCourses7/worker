package executor.service.executionService;

import executor.service.executor.ExecutionService;
import executor.service.model.ThreadPoolConfig;
import executor.service.scenario.ScenarioExecutor;
import executor.service.scenario.ScenarioSourceListenerImpl;
import executor.service.webdriver.WebDriverInitializer;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParallelFlowExecutorService {

    private ThreadPoolConfig threadPoolConfig;
    private ThreadPoolExecutor threadPoolExecutor;
    private final ScenarioSourceListenerImpl scenarioSourceListener;
    private final ExecutionService executionService;
    private final WebDriver webDriver;
    private final ScenarioExecutor scenarioExecutor;
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
        this.threadPoolConfig = init();
    }
    public ThreadPoolConfig init() {
        LOGGER.info("Get thread pool properties from file: " + "executorService.properties");
        Parameters parameters = new Parameters();
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                        .configure(parameters.properties()
                                .setFileName("executorService.properties"));
        try {
            Configuration configuration = builder.getConfiguration();
            return threadPoolConfig =
                    new ThreadPoolConfig(configuration.getInt("executorservice.common.threadsCount"),
                            configuration.getLong("executorservice.common.pageLoadTimeout"));
        } catch (ConfigurationException configException) {
            String message = "Configuration fail from file: " + "executorService.properties";
            LOGGER.error(message);
            configException.getStackTrace();
        }
        return threadPoolConfig;
    }
    public void startThreads() {
        LinkedBlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>();
        threadPoolExecutor = new ThreadPoolExecutor(threadPoolConfig.getCorePoolSize(),
                10,
                threadPoolConfig.getKeepAliveTime(),
                TimeUnit.MILLISECONDS,
                blockingQueue);
        LOGGER.info("Create new ThreadPoolExecutor with parameters: " +
                "CorePoolSize - " + threadPoolConfig.getCorePoolSize() +
                " MaxPoolSize - " + 10 +
                " KeepAliveTime - " + threadPoolConfig.getKeepAliveTime() +
                " milliseconds");
        while(!scenarioSourceListener.getScenarios().isEmpty()){

            LOGGER.info("Start executing scenarios in threads");
            threadPoolExecutor.execute(() -> executionService.execute(webDriver, scenarioSourceListener, scenarioExecutor));
        }
        LOGGER.info("Shutdown");
        threadPoolExecutor.shutdown();
    }

    public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }
}

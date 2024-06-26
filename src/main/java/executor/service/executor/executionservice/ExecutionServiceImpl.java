package executor.service.executor.executionservice;

import executor.service.listener.ScenarioSourceListener;
import executor.service.model.Scenario;
import executor.service.executor.scenarioexecutor.ScenarioExecutor;
import executor.service.webdriver.WebDriverInitializer;
import org.openqa.selenium.WebDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ExecutionServiceImpl implements ExecutionService {
    private static final Logger logger = LogManager.getLogger(ExecutionServiceImpl.class.getName());

    @Override
    public void execute(WebDriverInitializer webDriverInitializer,
                        ScenarioSourceListener scenarioSourceListener,
                        ScenarioExecutor scenarioExecutor) {
        Scenario scenario = scenarioSourceListener.getScenario();

        if (scenario == null || scenario.getSteps().isEmpty()) {
            logger.info("Scenario is empty.");
            return;
        }


        WebDriver driver = null;
        try {
            driver = webDriverInitializer.init();

            logger.info("Starting execution for scenario: {}", scenario.getName());

            String site = scenario.getSite();
            logger.info("Navigating to site: {}", site);
            if (!site.startsWith("http")) site = "http://" + site;
            driver.get(site);

            logger.info("Executing scenario...");
            scenarioExecutor.execute(scenario, driver);
        } catch (Exception e) {
            scenarioSourceListener.addScenario(scenario);
            logger.error("Exception occurred during scenario execution: ", e);
        } finally {
            if (driver != null) {
                logger.info("Quitting WebDriver...");
                driver.quit();
            }
        }
    }
}
package executor.service.executor.executionservice;

import executor.service.listener.ScenarioSourceListener;
import executor.service.model.Scenario;
import executor.service.executor.scenarioexecutor.ScenarioExecutor;
import org.openqa.selenium.WebDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ExecutionServiceImpl implements ExecutionService {
    private static final Logger logger = LogManager.getLogger(ExecutionServiceImpl.class.getName());

    @Override
    public void execute(WebDriver driver, ScenarioSourceListener scenarioSourceListener, ScenarioExecutor scenarioExecutor) {
        Scenario scenario = scenarioSourceListener.getScenario();

        if (scenario == null || scenario.getSteps().isEmpty()) {
            logger.info("Scenario is empty.");
            return;
        }

        try {

            logger.info("Starting execution for scenario: {}", scenario.getName());

            logger.info("Navigating to site: {}", scenario.getSite());
            driver.get(scenario.getSite());

            logger.info("Executing scenario...");
            scenarioExecutor.execute(scenario, driver);
        } catch (Exception e) {
            logger.error("Exception occurred during scenario execution: ", e);
        } finally {
            logger.info("Quitting WebDriver...");
            driver.quit();
        }
    }
}
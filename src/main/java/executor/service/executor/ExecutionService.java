package executor.service.executor;

import executor.service.model.Scenario;
import executor.service.scenario.ScenarioExecutor;
import executor.service.scenario.ScenarioSourceListenerImpl;
import org.openqa.selenium.WebDriver;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExecutionService {
    private static final Logger logger = Logger.getLogger(ExecutionService.class.getName());

    public void execute(WebDriver driver, ScenarioSourceListenerImpl sourceListener, ScenarioExecutor scenarioExecutor) {

        try {

            Scenario scenario = sourceListener.getScenario();

            logger.info("Starting execution for scenario: " + scenario.getName());

            logger.info("Navigating to site: " + scenario.getSite());
            driver.get(scenario.getSite());

            logger.info("Executing scenario...");
            scenarioExecutor.execute(scenario, driver);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception occurred during scenario execution: ", e);
        } finally {
            logger.info("Quitting WebDriver...");
            driver.quit();
        }
    }
}
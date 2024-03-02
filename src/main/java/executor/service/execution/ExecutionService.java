package executor.service.execution;

import executor.service.model.Scenario;
import executor.service.scenario.ScenarioExecutor;
import org.openqa.selenium.WebDriver;

public class ExecutionService {
    public void execute(WebDriver webDriver, ScenarioExecutor scenarioExecutor) {
        scenarioExecutor.execute(new Scenario(), webDriver);
    }
}

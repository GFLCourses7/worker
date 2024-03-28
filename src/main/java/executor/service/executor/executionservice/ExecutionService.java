package executor.service.executor.executionservice;

import executor.service.executor.scenarioexecutor.ScenarioExecutor;
import executor.service.model.Scenario;
import org.openqa.selenium.WebDriver;

public interface ExecutionService {
    void execute(WebDriver driver, Scenario scenario, ScenarioExecutor scenarioExecutor);
}

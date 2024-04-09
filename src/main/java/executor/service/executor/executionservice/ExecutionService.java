package executor.service.executor.executionservice;

import executor.service.executor.scenarioexecutor.ScenarioExecutor;
import executor.service.listener.ScenarioSourceListener;
import org.openqa.selenium.WebDriver;

public interface ExecutionService {
    void execute(WebDriver driver, ScenarioSourceListener scenarioSourceListener, ScenarioExecutor scenarioExecutor);
}

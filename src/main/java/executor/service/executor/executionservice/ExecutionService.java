package executor.service.executor.executionservice;

import executor.service.executor.scenarioexecutor.ScenarioExecutor;
import executor.service.listener.ScenarioSourceListener;
import executor.service.webdriver.WebDriverInitializer;
import org.openqa.selenium.WebDriver;

public interface ExecutionService {
    void execute(WebDriverInitializer webDriverInitializer,
                 ScenarioSourceListener scenarioSourceListener,
                 ScenarioExecutor scenarioExecutor);
}

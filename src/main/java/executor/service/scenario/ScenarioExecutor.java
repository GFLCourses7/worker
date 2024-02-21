package executor.service.scenario;

import executor.service.model.Scenario;
import org.openqa.selenium.WebDriver;

public interface ScenarioExecutor {

    void execute(Scenario scenario, WebDriver webDriver);

}

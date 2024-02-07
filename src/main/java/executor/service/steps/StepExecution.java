package executor.service.steps;

import executor.service.model.Step;
import org.openqa.selenium.WebDriver;

public interface StepExecution {
    String getStepAction();
    void step(WebDriver webDriver, Step step);
}

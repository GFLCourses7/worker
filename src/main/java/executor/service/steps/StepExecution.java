package executor.service.steps;

import executor.service.model.StepDTO;
import org.openqa.selenium.WebDriver;

public interface StepExecution {
    String getStepAction();
    void step(WebDriver webDriver, StepDTO step);
}

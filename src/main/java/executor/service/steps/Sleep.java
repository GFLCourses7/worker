package executor.service.steps;

import executor.service.model.Step;
import executor.service.utils.StepAction;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

public class Sleep implements StepExecution{
    @Override
    public String getStepAction() {
        return StepAction.SLEEP.label;
    }

    @Override
    public void step(WebDriver webDriver, Step step) {
        try {
            long sleep = Long.parseLong(step.getValue());
            TimeUnit.MILLISECONDS.sleep(sleep);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}

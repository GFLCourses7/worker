package executor.service.steps;

import executor.service.model.Step;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

public class Sleep implements StepExecution{
    @Override
    public String getStepAction() {
        return Sleep.class.getSimpleName();
    }

    @Override
    public void step(WebDriver webDriver, Step step) {
        long sleep = Long.parseLong(step.getValue());
        try {
            TimeUnit.MILLISECONDS.sleep(sleep);
        } catch (InterruptedException e) {
            System.out.println("Interrupted Exception: " + e.getMessage());
        }
    }
}

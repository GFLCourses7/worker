package executor.service.steps;

import executor.service.model.Step;
import executor.service.utils.StepAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
@Service
public class Sleep implements StepExecution{
    private static final Logger LOGGER = LogManager.getLogger(Sleep.class.getName());
    @Override
    public String getStepAction() {
        return StepAction.SLEEP.label;
    }

    @Override
    public void step(WebDriver webDriver, Step step) {
        try {
            long sleep = Long.parseLong(step.getValue());
            TimeUnit.MILLISECONDS.sleep(sleep);
        } catch (InterruptedException | NumberFormatException e) {
            LOGGER.error(e);
        }
    }
}


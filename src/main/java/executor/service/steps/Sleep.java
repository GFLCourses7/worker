package executor.service.steps;

import executor.service.model.Step;
import executor.service.utils.StepAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class Sleep implements StepExecution {
    private static final Logger LOGGER = LogManager.getLogger(Sleep.class.getName());

    @Override
    public String getStepAction() {
        return StepAction.SLEEP.label;
    }

    @Override
    public void step(WebDriver driver, Step step) {
        try {
            long sleep = Long.parseLong(step.getValue());
            if (sleep < 0) {
                throw new IllegalArgumentException("Sleep time cannot be negative");
            }
            TimeUnit.MILLISECONDS.sleep(sleep);
        } catch (NumberFormatException e) {
            LOGGER.error("Invalid sleep value: {}", step.getValue(), e);
        } catch (IllegalArgumentException | InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
    }
}


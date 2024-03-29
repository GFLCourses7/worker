package executor.service.factory.stepexecutionfactory;

import executor.service.steps.ClickCss;
import executor.service.steps.ClickXpath;
import executor.service.steps.Sleep;
import executor.service.steps.StepExecution;
import executor.service.utils.StepAction;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class StepExecutionFactoryDefault implements StepExecutionFactory {

    private final ClickCss clickCss;
    private final ClickXpath clickXpath;
    private final Sleep sleep;

    public StepExecutionFactoryDefault(ClickCss clickCss, ClickXpath clickXpath, Sleep sleep) {
        this.clickCss = clickCss;
        this.clickXpath = clickXpath;
        this.sleep = sleep;
    }

    public StepExecution createStepExecution(String stepAction) {

        // Search for action in enums
        Optional<StepAction> optionalStepAction = Arrays.stream(StepAction.values())
                .filter(enumObj -> enumObj.label.equals(stepAction))
                .findFirst();

        // Replace with proper logging later
        if (optionalStepAction.isEmpty())
            throw new IllegalArgumentException(String.format("Value %s doesn't match any step.", stepAction));

        StepExecution stepExecution = null;
        switch (optionalStepAction.get()) {
            case CLICK_CSS -> {
                stepExecution = clickCss;
            }
            case CLICK_XPATH -> {
                stepExecution = clickXpath;
            }
            case SLEEP -> {
                stepExecution = sleep;
            }
        }

        return stepExecution;
    }

}

package executor.service.factory.stepexecutionfactory;

import executor.service.steps.ClickCss;
import executor.service.steps.ClickXpath;
import executor.service.steps.Sleep;
import executor.service.steps.StepExecution;
import executor.service.utils.StepAction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
@Scope("prototype")
public class StepExecutionFactoryDefault implements StepExecutionFactory {

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
                stepExecution = new ClickCss();
            }
            case CLICK_XPATH -> {
                stepExecution = new ClickXpath();
            }
            case SLEEP -> {
                stepExecution = new Sleep();
            }
        }

        return stepExecution;
    }

}

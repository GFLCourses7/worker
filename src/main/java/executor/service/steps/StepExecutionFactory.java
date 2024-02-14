package executor.service.steps;

import executor.service.utils.StepAction;

import java.util.Arrays;
import java.util.Optional;

public class StepExecutionFactory {

    public StepExecution createStepExecution(String stepAction) {

        // Search for action in enums
        Optional<StepAction> optionalStepAction = Arrays.stream(StepAction.values())
                .filter(enumObj -> enumObj.label.equals(stepAction))
                .findFirst();

        // Replace with proper logging later
        if (!optionalStepAction.isPresent())
            throw new RuntimeException(String.format("ERROR: Value %s doesn't match any step.", stepAction));


        StepExecution stepExecution = null;
        switch (optionalStepAction.get()) {

            case CLICK_CSS: {
                stepExecution = new ClickCss();
                break;
            }
            // More actions need to be added...

        }

        return stepExecution;
    }

}

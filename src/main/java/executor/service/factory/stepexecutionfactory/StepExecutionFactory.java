package executor.service.factory.stepexecutionfactory;

import executor.service.steps.StepExecution;

public interface StepExecutionFactory {

    StepExecution createStepExecution(String stepAction);

}

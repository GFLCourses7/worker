package executor.service.steps;

public interface StepExecutionFactory {

    StepExecution createStepExecution(String stepAction);

}

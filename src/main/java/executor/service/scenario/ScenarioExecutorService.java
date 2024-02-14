package executor.service.scenario;

import executor.service.model.Scenario;
import executor.service.steps.StepExecution;
import executor.service.steps.StepExecutionFactory;
import org.openqa.selenium.WebDriver;

public class ScenarioExecutorService implements ScenarioExecutor {

    @Override
    public void execute(Scenario scenario, WebDriver webDriver) {

        StepExecutionFactory stepExecutionFactory = new StepExecutionFactory();
        scenario.getSteps().forEach(step -> {
            StepExecution stepExecution = stepExecutionFactory.createStepExecution(step.getAction());
            stepExecution.step(webDriver, step);
        });
    }
}

package executor.service.executor.scenarioexecutor;

import executor.service.model.Scenario;
import executor.service.steps.StepExecution;
import executor.service.factory.stepexecutionfactory.StepExecutionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

@Service
public class ScenarioExecutorService implements ScenarioExecutor {

    private static final Logger logger = LogManager.getLogger(ScenarioExecutorService.class);

    private final StepExecutionFactory stepExecutionFactory;

    public ScenarioExecutorService(StepExecutionFactory stepExecutionFactory) {
        this.stepExecutionFactory = stepExecutionFactory;
    }

    @Override
    public void execute(Scenario scenario, WebDriver webDriver) {
        scenario.getSteps().forEach(step -> {
            // Special on error behaviour can be set later,
            // for now, ignore step, log error and move to
            // the next one
            try {
                StepExecution stepExecution = stepExecutionFactory.createStepExecution(step.getAction());
                stepExecution.step(webDriver, step);
            } catch (IllegalArgumentException e) {
                logger.error(e.getMessage());
            }
        });
    }
}

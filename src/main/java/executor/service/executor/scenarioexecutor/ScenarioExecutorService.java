package executor.service.executor.scenarioexecutor;

import executor.service.model.Scenario;
import executor.service.model.ScenarioWrapper;
import executor.service.service.ResultClientService;
import executor.service.steps.StepExecution;
import executor.service.factory.stepexecutionfactory.StepExecutionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ScenarioExecutorService implements ScenarioExecutor {

    private static final Logger LOGGER = LogManager.getLogger(ScenarioExecutorService.class);

    private final StepExecutionFactory stepExecutionFactory;

    private final ResultClientService resultClientService;

    public ScenarioExecutorService(StepExecutionFactory stepExecutionFactory, ResultClientService resultClientService) {
        this.stepExecutionFactory = stepExecutionFactory;
        this.resultClientService = resultClientService;
    }

    @Override
    public void execute(Scenario scenario, WebDriver webDriver) {

        StringBuffer log = new StringBuffer();

        scenario.getSteps().forEach(step -> {
            // Special on error behaviour can be set later,
            // for now, ignore step, log error and move to
            // the next one
            try {
                StepExecution stepExecution = stepExecutionFactory.createStepExecution(step.getAction());
                log.append(String.format("INFO: performing action: %s ( value: %s )\n", step.getAction(), step.getValue()));
                LOGGER.info(String.format("performing action: %s ( value: %s )", step.getAction(), step.getValue()));

                stepExecution.step(webDriver, step);
                log.append("INFO: step execution success\n");
                LOGGER.info("step execution success");

            } catch (Exception e) {

                log.append("ERROR: step execution failed\n");
                LOGGER.error("step execution failed " + e.getMessage());
            }
        });
        LOGGER.info("finished scenario step execution");

        sendResult(scenario, log.toString());
    }

    private void sendResult(Scenario scenario, String log) {

        ScenarioWrapper scenarioWrapper = (ScenarioWrapper) scenario;
        scenarioWrapper.setResult(log);

        try {
            LOGGER.info("sending result to client");
            resultClientService.sendResult(scenarioWrapper);
            LOGGER.info("result sent successfully");
        } catch (IOException e) {
            LOGGER.error("result request failed " + e);
        }
    }
}

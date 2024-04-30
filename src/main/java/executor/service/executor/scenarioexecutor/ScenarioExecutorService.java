package executor.service.executor.scenarioexecutor;

import executor.service.factory.stepexecutionfactory.StepExecutionFactory;
import executor.service.model.Scenario;
import executor.service.model.ScenarioWrapper;
import executor.service.okhttp.ClientService;
import executor.service.steps.StepExecution;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ScenarioExecutorService implements ScenarioExecutor {

    private static final Logger LOGGER = LogManager.getLogger(ScenarioExecutorService.class);

    private final StepExecutionFactory stepExecutionFactory;

    private final ClientService clientService;

    public ScenarioExecutorService(StepExecutionFactory stepExecutionFactory, ClientService clientService) {
        this.stepExecutionFactory = stepExecutionFactory;
        this.clientService = clientService;
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
                LOGGER.info(String.format("Performing action: %s ( value: %s )", step.getAction(), step.getValue()));

                stepExecution.step(webDriver, step);
                log.append("INFO: step execution success\n");
                LOGGER.info("Step execution success");

            } catch (Exception e) {

                String error = e.getMessage();
                log.append("ERROR: step execution failed: ")
                        .append(error, 0, error.indexOf("\n"))
                        .append("\n");
                LOGGER.error("Step execution failed {}", error);
            }
        });
        LOGGER.info("Finished scenario step execution");

        sendResult(scenario, log.toString());
    }

    private void sendResult(Scenario scenario, String log) {

        ScenarioWrapper scenarioWrapper = (ScenarioWrapper) scenario;
        scenarioWrapper.setResult(log);

        try {
            LOGGER.info("Sending result to client");
            clientService.sendResult(scenarioWrapper);
            LOGGER.info("Result sent successfully");
        } catch (IOException e) {
            LOGGER.error("Result request failed {}", e.getMessage());
        }
    }
}

package executor.service.executor.scenarioexecutor;

import executor.service.factory.stepexecutionfactory.StepExecutionFactory;
import executor.service.model.Scenario;
import executor.service.model.ScenarioWrapper;
import executor.service.model.Step;
import executor.service.okhttp.ClientService;
import executor.service.steps.*;
import executor.service.utils.StepAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class ScenarioExecutorServiceTest {


    @Test
    public void executeTestClickCss() {

        // Create mocked ClickCss
        StepExecution mockedClickCss = mock(ClickCss.class);

        // Create step execution service with mocked factory that produces mocked ClickCss
        StepExecutionFactory mockedStepExecutorFactory = mock(StepExecutionFactory.class);
        when(mockedStepExecutorFactory.createStepExecution(StepAction.CLICK_CSS.label)).thenReturn(mockedClickCss);

        ClientService clientService = mock(ClientService.class);

        ScenarioExecutorService scenarioExecutorService = new ScenarioExecutorService(mockedStepExecutorFactory, clientService);

        // Create mocked webdriver
        WebDriver mockedWebDriver = mock(WebDriver.class);

        Scenario scenario = new ScenarioWrapper();

        List<Step> steps = new ArrayList<>();
        Step step = new Step();
        step.setAction(StepAction.CLICK_CSS.label);
        step.setValue("");
        steps.add(step);

        scenario.setSteps(steps);

        scenarioExecutorService.execute(scenario, mockedWebDriver);

        // Verify mocked StepExecution has been called
        verify(mockedClickCss, times(1)).step(any(), any());
    }

    @Test
    public void executeTestClickXpath() {

        // Create mocked mockedClickXpath
        StepExecution mockedClickXpath = mock(ClickXpath.class);

        // Create step execution service with mocked factory that produces mocked ClickXpath
        StepExecutionFactory mockedStepExecutorFactory = mock(StepExecutionFactory.class);
        when(mockedStepExecutorFactory.createStepExecution(StepAction.CLICK_XPATH.label)).thenReturn(mockedClickXpath);

        ClientService clientService = mock(ClientService.class);

        ScenarioExecutorService scenarioExecutorService = new ScenarioExecutorService(mockedStepExecutorFactory, clientService);

        // Create mocked webdriver
        WebDriver mockedWebDriver = mock(WebDriver.class);

        Scenario scenario = new ScenarioWrapper();

        List<Step> steps = new ArrayList<>();
        Step step = new Step();
        step.setAction(StepAction.CLICK_XPATH.label);
        step.setValue("");
        steps.add(step);

        scenario.setSteps(steps);

        scenarioExecutorService.execute(scenario, mockedWebDriver);

        // Verify mocked StepExecution has been called
        verify(mockedClickXpath, times(1)).step(any(), any());
    }

    @Test
    public void executeTestSleep() {

        // Create mocked mockedSleep
        StepExecution mockedSleep = mock(Sleep.class);

        // Create step execution service with mocked factory that produces mocked Sleep
        StepExecutionFactory mockedStepExecutorFactory = mock(StepExecutionFactory.class);
        when(mockedStepExecutorFactory.createStepExecution(StepAction.SLEEP.label)).thenReturn(mockedSleep);

        ClientService clientService = mock(ClientService.class);

        ScenarioExecutorService scenarioExecutorService = new ScenarioExecutorService(mockedStepExecutorFactory, clientService);

        // Create mocked webdriver
        WebDriver mockedWebDriver = mock(WebDriver.class);

        Scenario scenario = new ScenarioWrapper();

        List<Step> steps = new ArrayList<>();
        Step step = new Step();
        step.setAction(StepAction.SLEEP.label);
        step.setValue("");
        steps.add(step);

        scenario.setSteps(steps);

        scenarioExecutorService.execute(scenario, mockedWebDriver);

        // Verify mocked StepExecution has been called
        verify(mockedSleep, times(1)).step(any(), any());
    }

    @Test
    public void executeTestAll() {

        // Create all StepExecutions
        StepExecution mockedClickCss = mock(ClickCss.class);
        StepExecution mockedClickXpath = mock(ClickXpath.class);
        StepExecution mockedSleep = mock(Sleep.class);

        // Create step execution service with mocked factory that produces all mocked StepActions
        StepExecutionFactory mockedStepExecutorFactory = mock(StepExecutionFactory.class);
        when(mockedStepExecutorFactory.createStepExecution(StepAction.CLICK_CSS.label)).thenReturn(mockedClickCss);
        when(mockedStepExecutorFactory.createStepExecution(StepAction.CLICK_XPATH.label)).thenReturn(mockedClickXpath);
        when(mockedStepExecutorFactory.createStepExecution(StepAction.SLEEP.label)).thenReturn(mockedSleep);

        ClientService clientService = mock(ClientService.class);

        ScenarioExecutorService scenarioExecutorService = new ScenarioExecutorService(mockedStepExecutorFactory, clientService);

        // Create mocked webdriver
        WebDriver mockedWebDriver = mock(WebDriver.class);

        Scenario scenario = new ScenarioWrapper();

        List<Step> steps = new ArrayList<>();
        Step clickCssStep = new Step();
        clickCssStep.setAction(StepAction.CLICK_CSS.label);
        clickCssStep.setValue("");
        steps.add(clickCssStep);

        Step clickXpathStep = new Step();
        clickXpathStep.setAction(StepAction.CLICK_XPATH.label);
        clickXpathStep.setValue("");
        steps.add(clickXpathStep);

        Step sleepStep = new Step();
        sleepStep.setAction(StepAction.SLEEP.label);
        sleepStep.setValue("");
        steps.add(sleepStep);

        scenario.setSteps(steps);

        scenarioExecutorService.execute(scenario, mockedWebDriver);

        // Verify all mocked StepExecutions have been called
        verify(mockedClickCss, times(1)).step(any(), any());
        verify(mockedClickXpath, times(1)).step(any(), any());
        verify(mockedSleep, times(1)).step(any(), any());
    }

    @Test
    public void testResult() throws IOException {

        ScenarioWrapper scenario = new ScenarioWrapper();
        scenario.setId(1L);
        scenario.setName("Test Scenario");
        scenario.setSite("https://www.example.com");
        scenario.setSteps(List.of(new Step(StepAction.SLEEP.name(), "10")));

        WebDriver webDriver = mock(WebDriver.class);

        StepExecutionFactory stepExecutionFactory = mock(StepExecutionFactory.class);
        when(stepExecutionFactory.createStepExecution(any())).thenReturn(new Sleep());

        ClientService clientService = mock(ClientService.class);

        ScenarioExecutorService scenarioExecutorService = new ScenarioExecutorService(stepExecutionFactory,
                clientService);

        scenarioExecutorService.execute(scenario, webDriver);

        String expected = """
                INFO: performing action: SLEEP ( value: 10 )
                INFO: step execution success
                """;
        String actual = scenario.getResult();

        Assertions.assertEquals(expected, actual);
    }
}

package executor.service.executor.scenarioexecutor;

import executor.service.executor.scenarioexecutor.ScenarioExecutorService;
import executor.service.factory.stepexecutionfactory.StepExecutionFactory;
import executor.service.model.Scenario;
import executor.service.model.Step;
import executor.service.steps.*;
import executor.service.utils.StepAction;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

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
        ScenarioExecutorService scenarioExecutorService = new ScenarioExecutorService(mockedStepExecutorFactory);

        // Create mocked webdriver
        WebDriver mockedWebDriver = mock(WebDriver.class);

        Scenario scenario = new Scenario();

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
        ScenarioExecutorService scenarioExecutorService = new ScenarioExecutorService(mockedStepExecutorFactory);

        // Create mocked webdriver
        WebDriver mockedWebDriver = mock(WebDriver.class);

        Scenario scenario = new Scenario();

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
        ScenarioExecutorService scenarioExecutorService = new ScenarioExecutorService(mockedStepExecutorFactory);

        // Create mocked webdriver
        WebDriver mockedWebDriver = mock(WebDriver.class);

        Scenario scenario = new Scenario();

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
        ScenarioExecutorService scenarioExecutorService = new ScenarioExecutorService(mockedStepExecutorFactory);

        // Create mocked webdriver
        WebDriver mockedWebDriver = mock(WebDriver.class);

        Scenario scenario = new Scenario();

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

}

package executor.service.executor.executionservice;

import com.google.common.collect.Lists;
import executor.service.executor.scenarioexecutor.ScenarioExecutor;
import executor.service.listener.ScenarioSourceListener;
import executor.service.model.Scenario;
import executor.service.model.ScenarioWrapper;
import executor.service.model.Step;
import executor.service.utils.StepAction;
import executor.service.webdriver.WebDriverInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.WebDriver;

import java.util.List;

import static org.mockito.Mockito.*;

public class ExecutionServiceTest {

    @Mock
    private ScenarioSourceListener scenarioSourceListener;
    @Mock
    private ScenarioExecutor scenarioExecutor;
    @Mock
    private WebDriverInitializer webDriverInitializer;

    private ExecutionService executionService;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);
        executionService = new ExecutionServiceImpl();
    }

    @Test
    public void testNotExecuteWithNullScenario() {

        when(scenarioSourceListener.getScenario()).thenReturn(null);

        WebDriver mockWebDriver = mock(WebDriver.class);
        when(webDriverInitializer.init()).thenReturn(mockWebDriver);

        executionService.execute(webDriverInitializer, scenarioSourceListener, scenarioExecutor);

        verify(mockWebDriver, never()).get(anyString());
        verify(scenarioExecutor, never()).execute(any(Scenario.class), any(WebDriver.class));
//        verify(driver, times(1)).quit();
    }

    @Test
    public void testNotExecuteWithEmptyScenarioEmptySteps() {

        when(scenarioSourceListener.getScenario()).thenReturn(new Scenario("", "", Lists.newArrayListWithExpectedSize(0)));

        WebDriver mockWebDriver = mock(WebDriver.class);
        when(webDriverInitializer.init()).thenReturn(mockWebDriver);

        executionService.execute(webDriverInitializer, scenarioSourceListener, scenarioExecutor);

        verify(mockWebDriver, never()).get(anyString());
        verify(scenarioExecutor, never()).execute(any(Scenario.class), any(WebDriver.class));
//        verify(driver, times(1)).quit();
    }

    @Test
    public void testExecuteWithValidScenarioAndSteps() {

        Scenario scenario = new Scenario("Test Scenario", "https://www.example.com", List.of(new Step(StepAction.SLEEP.name(), "10")));
        when(scenarioSourceListener.getScenario()).thenReturn(scenario);

        WebDriver mockWebDriver = mock(WebDriver.class);
        when(webDriverInitializer.init()).thenReturn(mockWebDriver);

        executionService.execute(webDriverInitializer, scenarioSourceListener, scenarioExecutor);

        verify(mockWebDriver, times(1)).get(scenario.getSite());
        verify(scenarioExecutor, times(1)).execute(scenario, mockWebDriver);
        verify(mockWebDriver, times(1)).quit();
    }
}

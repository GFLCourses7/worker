package executor.service.executor;

import executor.service.model.Scenario;
import executor.service.scenario.ScenarioExecutor;
import executor.service.scenario.ScenarioSourceListenerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;

import static org.mockito.Mockito.*;

public class ExecutionServiceTest {

    private WebDriver mockedDriver;
    private ScenarioSourceListenerImpl mockedSourceListener;
    private ScenarioExecutor mockedScenarioExecutor;

    @BeforeEach
    void setUp() {
        mockedDriver = Mockito.mock(WebDriver.class);
        mockedSourceListener = Mockito.mock(ScenarioSourceListenerImpl.class);
        mockedScenarioExecutor = Mockito.mock(ScenarioExecutor.class);
    }

    @Test
    public void testExecute() {
        Scenario scenario = new Scenario();
        scenario.setName("Test Scenario");
        scenario.setSite("http://example.com");
        when(mockedSourceListener.getScenario()).thenReturn(scenario);

        ExecutionService executionService = new ExecutionService();

        executionService.execute(mockedDriver, mockedSourceListener, mockedScenarioExecutor);

        verify(mockedDriver).get("http://example.com");
        verify(mockedScenarioExecutor).execute(scenario, mockedDriver);
        verify(mockedDriver).quit();
    }

    @Test
    public void testMandatoryQuitAfterException() {
        when(mockedSourceListener.getScenario()).thenReturn(null);
        ExecutionService executionService = new ExecutionService();
        executionService.execute(mockedDriver, mockedSourceListener, mockedScenarioExecutor);
        try {
            executionService.execute(mockedDriver, mockedSourceListener, mockedScenarioExecutor);
        } catch (Exception e) {
            // Перевірка, чи був викликаний метод quit() для закриття драйвера
            verify(mockedDriver, times(1)).quit();
        }
    }
}
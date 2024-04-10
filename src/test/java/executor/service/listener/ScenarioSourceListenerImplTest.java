package executor.service.listener;

import executor.service.model.Scenario;
import executor.service.utils.configreader.ConfigReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ScenarioSourceListenerImplTest {

    @Mock
    private ConfigReader configReader;

    private ScenarioSourceListenerImpl scenarioSourceListener;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetScenario_WithScenariosAvailable() {
        Scenario scenario = new Scenario();

        List<Scenario> scenarios = new ArrayList<>();
        scenarios.add(scenario);

        when(configReader.readFile(anyString(), eq(Scenario.class))).thenReturn(scenarios);

        scenarioSourceListener = new ScenarioSourceListenerImpl();

        Scenario result = scenarioSourceListener.getScenario();

        assertEquals(scenario, result);
    }

    @Test
    public void testGetScenarioIsBlockedWithNoScenariosAvailable() {
        when(configReader.readFile(anyString(), eq(Scenario.class))).thenReturn(new ArrayList<>());

        scenarioSourceListener = new ScenarioSourceListenerImpl();
//        Scenario result = scenarioSourceListener.getScenario();
//
//        assertNull(result);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Scenario> future = executor.submit(() -> scenarioSourceListener.getScenario());

        try {
            Scenario result = future.get(5, TimeUnit.SECONDS);
            assertNull(result);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            System.out.println("Queue is empty, thread is blocked.");
            assertInstanceOf(TimeoutException.class, e);
        } finally {
            executor.shutdown();
        }
    }

    @Test
    public void testAddScenario() {
        Scenario scenario = new Scenario();

        scenarioSourceListener = new ScenarioSourceListenerImpl();
        scenarioSourceListener.addScenario(scenario);

        Scenario actual = scenarioSourceListener.getScenario();
        assertEquals(scenario, actual);
    }
}

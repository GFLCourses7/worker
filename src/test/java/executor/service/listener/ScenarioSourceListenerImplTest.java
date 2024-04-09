package executor.service.listener;

import executor.service.model.Scenario;
import executor.service.utils.configreader.ConfigReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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

        scenarioSourceListener = new ScenarioSourceListenerImpl(configReader);

        Scenario result = scenarioSourceListener.getScenario();

        assertEquals(scenario, result);
    }

    @Test
    public void testGetScenario_WithNoScenariosAvailable() {
        when(configReader.readFile(anyString(), eq(Scenario.class))).thenReturn(new ArrayList<>());

        scenarioSourceListener = new ScenarioSourceListenerImpl(configReader);
        Scenario result = scenarioSourceListener.getScenario();

        assertNull(result);
    }

    @Test
    public void testAddScenario() {
        Scenario scenario = new Scenario();

        scenarioSourceListener = new ScenarioSourceListenerImpl(configReader);
        scenarioSourceListener.addScenario(scenario);

        Scenario actual = scenarioSourceListener.getScenario();
        assertEquals(scenario, actual);
    }
}

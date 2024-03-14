package executor.service.listener;

import executor.service.config.JsonConfigReader;
import executor.service.listener.ScenarioSourceListenerImpl;
import executor.service.model.Scenario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ScenarioSourceListenerImplTest {

    private ScenarioSourceListenerImpl listener;
    private static final String SCENARIOS_JSON = "scenarios.json";
    @BeforeEach
    public void setUp() {
        listener = new ScenarioSourceListenerImpl();
    }

    @Test
    public void testExecuteSuccess() throws URISyntaxException {
        // Prepare test data
        List<Scenario> fakeScenarios = new ArrayList<>();
        fakeScenarios.add(new Scenario("test scenario 1", "site1", new ArrayList<>()));
        fakeScenarios.add(new Scenario("test scenario 2", "site2", new ArrayList<>()));

        // Look for scenarios.json inside /resources folder
        String path = Objects.requireNonNull(getClass().getClassLoader().getResource(SCENARIOS_JSON)).toURI().getPath();

        try (MockedStatic<JsonConfigReader> utilities = mockStatic(JsonConfigReader.class)) {
            utilities.when(() -> JsonConfigReader.readFile(eq(path), eq(Scenario.class)))
                    .thenReturn(fakeScenarios);

            listener = new ScenarioSourceListenerImpl();

            assertEquals(fakeScenarios.get(0), listener.getScenario());
            assertNotNull(listener.getScenario());
    }
    }

    @Test
    public void testGetScenarioFromEmptyList() throws URISyntaxException {

        // Look for scenarios.json inside /resources folder
        String path = Objects.requireNonNull(getClass().getClassLoader().getResource(SCENARIOS_JSON)).toURI().getPath();

        try (MockedStatic<JsonConfigReader> utilities = mockStatic(JsonConfigReader.class)) {
            utilities.when(() -> JsonConfigReader.readFile(eq(path), eq(Scenario.class)))
                    .thenReturn(Collections.emptyList());

            listener = new ScenarioSourceListenerImpl();

            assertNull(listener.getScenario());
        }
    }

    @Test
    public void testGetScenario() throws InterruptedException {
        // Prepare test data
        Scenario scenario = new Scenario("Test Scenario", "Site", new ArrayList<>());

        // Add scenario to the queue
        listener.setScenarios(new LinkedBlockingQueue<>(Collections.singletonList(scenario)));

        // Execute the method under test
        Scenario retrievedScenario = listener.getScenario();

        // Assertions
        assertNotNull(retrievedScenario);
        assertEquals(scenario, retrievedScenario);
        assertTrue(listener.getScenarios().isEmpty()); // The queue should be empty after retrieval
    }
}

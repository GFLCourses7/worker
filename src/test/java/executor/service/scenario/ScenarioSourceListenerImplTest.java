package executor.service.scenario;

import executor.service.model.Scenario;
import executor.service.utils.JsonConfigReader;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


class ScenarioSourceListenerImplTest {

    private static final String SCENARIOS_JSON = "scenarios.json";

    private ScenarioSourceListenerImpl listener;

    @Test
    public void testExecute_Success() throws Exception {
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
    public void testGetScenario_EmptyList() throws Exception {

        // Look for scenarios.json inside /resources folder
        String path = Objects.requireNonNull(getClass().getClassLoader().getResource(SCENARIOS_JSON)).toURI().getPath();

        try (MockedStatic<JsonConfigReader> utilities = mockStatic(JsonConfigReader.class)) {
            utilities.when(() -> JsonConfigReader.readFile(eq(path), eq(Scenario.class)))
                    .thenReturn(Collections.emptyList());

            listener = new ScenarioSourceListenerImpl();

            assertThrows(NoSuchElementException.class, () -> listener.getScenario());
        }
    }
}
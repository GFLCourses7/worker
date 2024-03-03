package executor.service.scenario;

import executor.service.model.Scenario;
import executor.service.utils.JsonConfigReader;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


class ScenarioSourceListenerImplTest {

    private ScenarioSourceListenerImpl listener;

    @Test
    public void testExecute_Success() {
        List<Scenario> fakeScenarios = new ArrayList<>();
        fakeScenarios.add(new Scenario("test scenario 1", "site1", new ArrayList<>()));
        fakeScenarios.add(new Scenario("test scenario 2", "site2", new ArrayList<>()));

        try (MockedStatic<JsonConfigReader> utilities = mockStatic(JsonConfigReader.class)) {
            utilities.when(() -> JsonConfigReader.readFile(eq("scenarios.json"), eq(Scenario.class)))
                    .thenReturn(fakeScenarios);

            listener = new ScenarioSourceListenerImpl();

            assertEquals(fakeScenarios.get(0), listener.getScenario());
            assertNotNull(listener.getScenario());
        }
    }

    @Test
    public void testGetScenario_EmptyList() {
        try (MockedStatic<JsonConfigReader> utilities = mockStatic(JsonConfigReader.class)) {
            utilities.when(() -> JsonConfigReader.readFile(eq("scenarios.json"), eq(Scenario.class)))
                    .thenReturn(Collections.emptyList());

            listener = new ScenarioSourceListenerImpl();

            assertThrows(NoSuchElementException.class, () -> listener.getScenario());
        }
    }
}
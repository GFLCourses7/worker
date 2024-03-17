package executor.service.factory.difactory;

import executor.service.executor.parallelflowexecution.ParallelFlowExecutorService;
import executor.service.executor.executionservice.ExecutionServiceImpl;
import executor.service.factory.difactory.AbstractFactory;
import executor.service.factory.difactory.DIFactory;
import executor.service.model.Scenario;
import executor.service.executor.scenarioexecutor.ScenarioExecutor;
import executor.service.executor.scenarioexecutor.ScenarioExecutorService;
import executor.service.listener.ScenarioSourceListener;
import executor.service.listener.ScenarioSourceListenerImpl;
import executor.service.config.JsonConfigReader;
import executor.service.webdriver.ChromeDriverInitializer;
import executor.service.webdriver.WebDriverInitializer;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class DIFactoryTest {

    private static final String SCENARIOS_JSON = "scenarios.json";

    @BeforeEach
    public void beforeEach() throws Exception {

        resetContext();
    }

    @SuppressWarnings("unchecked")
    private void resetContext() throws Exception {

        Field contextField = DIFactory.class.getDeclaredField("context");
        contextField.setAccessible(true);
        Map<Class<?>, Object> context
                = (Map<Class<?>, Object>) contextField.get(null);
        context.clear();
    }

    // Tests to check whether context resets between runs
    // order doesn't matter
    @SuppressWarnings("unchecked")
    @Test
    public void testResetContextFirst() throws Exception {


        AbstractFactory abstractFactory = new DIFactory();

        // Look for scenarios.json inside /resources folder
        String path = Objects.requireNonNull(getClass().getClassLoader().getResource(SCENARIOS_JSON)).toURI().getPath();


        // Mocking of static method works only inside of this try with resources method
        try (MockedStatic<JsonConfigReader> utilities = mockStatic(JsonConfigReader.class)) {

            utilities.when(() -> JsonConfigReader.readFile(eq(path), eq(Scenario.class)))
                    .thenReturn(new ArrayList<>());

            abstractFactory.create(ScenarioSourceListener.class);
            abstractFactory.create(ScenarioSourceListener.class);
        }

        Field contextField = DIFactory.class.getDeclaredField("context");
        contextField.setAccessible(true);
        Map<Class<?>, Object> context
                = (Map<Class<?>, Object>) contextField.get(null);

        int expected = 1;
        int actual = context.size();

        assertEquals(expected, actual);
    }

    // Tests to check whether context resets between runs
    // order doesn't matter
    @SuppressWarnings("unchecked")
    @Test
    public void testResetContextSecond() throws Exception {


        new DIFactory();

        Field contextField = DIFactory.class.getDeclaredField("context");
        contextField.setAccessible(true);
        Map<Class<?>, Object> context
                = (Map<Class<?>, Object>) contextField.get(null);

        int expected = 0;
        int actual = context.size();

        assertEquals(expected, actual);
    }

    @Test
    public void testScenarioExecutor() {

        AbstractFactory abstractFactory = new DIFactory();

        Class<?> expected = ScenarioExecutorService.class;
        Class<?> actual = abstractFactory.create(ScenarioExecutor.class).getClass();

        assertEquals(expected, actual);

    }

    @Test
    public void testWebDriverInitializer() {

        AbstractFactory abstractFactory = new DIFactory();

        Class<?> expectedValue = ChromeDriverInitializer.class;
        Class<?> actualValue = abstractFactory.create(WebDriverInitializer.class).getClass();

        assertEquals(expectedValue, actualValue);

    }

    @Test
    public void testScenarioSourceListener() throws Exception {


        AbstractFactory abstractFactory = new DIFactory();

        Class<?> expected;
        Class<?> actual;

        // Look for scenarios.json inside /resources folder
        String path = Objects.requireNonNull(getClass().getClassLoader().getResource(SCENARIOS_JSON)).toURI().getPath();


        // Mocking of static method works only inside of this try with resources method
        try (MockedStatic<JsonConfigReader> utilities = mockStatic(JsonConfigReader.class)) {

            utilities.when(() -> JsonConfigReader.readFile(eq(path), eq(Scenario.class)))
                    .thenReturn(new ArrayList<>());

            expected = ScenarioSourceListenerImpl.class;
            actual = abstractFactory.create(ScenarioSourceListener.class).getClass();
        }

        // Check whether object were created
        assertNotNull(expected);
        assertNotNull(actual);

        assertEquals(expected, actual);
    }

    @Test
    public void testScenarioSourceListenerSingleton() throws Exception {


        AbstractFactory abstractFactory = new DIFactory();

        ScenarioSourceListener expected;
        ScenarioSourceListener actual;

        // Look for scenarios.json inside /resources folder
        String path = Objects.requireNonNull(getClass().getClassLoader().getResource(SCENARIOS_JSON)).toURI().getPath();

        // Mocking of static method works only inside of this try with resources method
        try (MockedStatic<JsonConfigReader> utilities = mockStatic(JsonConfigReader.class)) {

            utilities.when(() -> JsonConfigReader.readFile(eq(path), eq(Scenario.class)))
                    .thenReturn(new ArrayList<>());

            expected = abstractFactory.create(ScenarioSourceListener.class);
            actual = abstractFactory.create(ScenarioSourceListener.class);
        }

        // Check whether object were created
        assertNotNull(expected);
        assertNotNull(actual);

        assertSame(expected, actual);
    }

    @Test
    public void testIncorrectType() {

        AbstractFactory abstractFactory = new DIFactory();

        String actual = abstractFactory.create(String.class);

        assertNull(actual);
    }

    @Test
    public void testExecutionService() {

        AbstractFactory abstractFactory = new DIFactory();

        ExecutionServiceImpl executionService = abstractFactory.create(ExecutionServiceImpl.class);

        Class<?> expected = ExecutionServiceImpl.class;
        Class<?> actual = executionService.getClass();

        assertEquals(expected, actual);
    }

    @Test
    public void testExecutionServiceSingleton() {

        AbstractFactory abstractFactory = new DIFactory();

        ExecutionServiceImpl expected = abstractFactory.create(ExecutionServiceImpl.class);
        ExecutionServiceImpl actual = abstractFactory.create(ExecutionServiceImpl.class);

        assertSame(expected, actual);
    }

    @Test
    public void testParallelFlowExecutorService() throws URISyntaxException {

        AbstractFactory abstractFactory = new DIFactory();

        Class<?> expected;
        Class<?> actual;

        // Look for scenarios.json inside /resources folder
        String path = Objects.requireNonNull(getClass().getClassLoader().getResource(SCENARIOS_JSON)).toURI().getPath();

        // Mocking of static method works only inside of this try with resources method
        try (MockedStatic<JsonConfigReader> utilities = mockStatic(JsonConfigReader.class)) {

            utilities.when(() -> JsonConfigReader.readFile(eq(path), eq(Scenario.class)))
                    .thenReturn(new ArrayList<>());

            expected = ParallelFlowExecutorService.class;
            actual = abstractFactory.create(ParallelFlowExecutorService.class).getClass();
        }

        // Check whether object were created
        assertNotNull(expected);
        assertNotNull(actual);

        assertEquals(expected, actual);

    }

    @Test
    public void testParallelFlowExecutorServiceSingleton() throws URISyntaxException {

        AbstractFactory abstractFactory = new DIFactory();

        Class<?> expected;
        Class<?> actual;

        // Look for scenarios.json inside /resources folder
        String path = Objects.requireNonNull(getClass().getClassLoader().getResource(SCENARIOS_JSON)).toURI().getPath();

        // Mocking of static method works only inside of this try with resources method
        try (MockedStatic<JsonConfigReader> utilities = mockStatic(JsonConfigReader.class)) {

            utilities.when(() -> JsonConfigReader.readFile(eq(path), eq(Scenario.class)))
                    .thenReturn(new ArrayList<>());

            expected = abstractFactory.create(ParallelFlowExecutorService.class).getClass();
            actual = abstractFactory.create(ParallelFlowExecutorService.class).getClass();
        }

        // Check whether object were created
        assertNotNull(expected);
        assertNotNull(actual);

        assertSame(expected, actual);

    }

}

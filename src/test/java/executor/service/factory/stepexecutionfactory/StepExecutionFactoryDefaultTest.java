package executor.service.factory.stepexecutionfactory;

import executor.service.factory.stepexecutionfactory.StepExecutionFactory;
import executor.service.factory.stepexecutionfactory.StepExecutionFactoryDefault;
import executor.service.steps.StepExecution;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class StepExecutionFactoryDefaultTest {

    public static StepExecutionFactory stepExecutionFactoryDefault;

    @BeforeEach
    public void initFactory() {
        stepExecutionFactoryDefault = new StepExecutionFactoryDefault();
    }

    @Test
    public void createStepExecutionClickCssTest() {

        String expectedValue = "clickCss";
        String actualValue;

        StepExecution stepExecution = stepExecutionFactoryDefault.createStepExecution(expectedValue);
        actualValue = stepExecution.getStepAction();

        assertEquals(expectedValue, actualValue);

    }

    @Test
    public void createStepExecutionClickXpathTest() {

        String expectedValue = "clickXpath";
        String actualValue;

        StepExecution stepExecution = stepExecutionFactoryDefault.createStepExecution(expectedValue);
        actualValue = stepExecution.getStepAction();

        assertEquals(expectedValue, actualValue);

    }

    @Test
    public void createStepExecutionSleepTest() {

        String expectedValue = "sleep";
        String actualValue;

        StepExecution stepExecution = stepExecutionFactoryDefault.createStepExecution(expectedValue);
        actualValue = stepExecution.getStepAction();

        assertEquals(expectedValue, actualValue);

    }

    @Test
    public void createStepExecutionNonExistentActionTest() {

        String nonExistentAction = "nonExistentAction";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            stepExecutionFactoryDefault.createStepExecution(nonExistentAction);
        });

        String expectedValue = String.format("Value %s doesn't match any step.", nonExistentAction);
        String actualValue = exception.getMessage();

        assertEquals(expectedValue, actualValue);

    }

}

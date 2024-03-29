package executor.service.factory.stepexecutionfactory;

import executor.service.steps.ClickCss;
import executor.service.steps.ClickXpath;
import executor.service.steps.Sleep;
import executor.service.steps.StepExecution;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class StepExecutionFactoryDefaultTest {

    public static StepExecutionFactory stepExecutionFactoryDefault;

    @BeforeEach
    public void initFactory() {
        stepExecutionFactoryDefault = new StepExecutionFactoryDefault(new ClickCss(), new ClickXpath(), new Sleep());
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

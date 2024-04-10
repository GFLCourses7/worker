package executor.service.steps;

import executor.service.model.Step;
import executor.service.utils.StepAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SleepTest {

    @Mock
    private WebDriver mockWebDriver;
    private Sleep sleepStep;
    private Step step;


    @BeforeEach
    void setUp() {
        sleepStep = new Sleep();

        step = new Step();
        step.setAction(StepAction.SLEEP.name());
        step.setValue("1000");
    }


    @Test
    public void testStepExecution_ValidSleepTime() {
        long startTime = System.currentTimeMillis();
        sleepStep.step(mockWebDriver, step);
        long endTime = System.currentTimeMillis();

        long elapsedTime = endTime - startTime;

        assertTrue(elapsedTime >= 1000, "Sleep duration was less than expected");
    }

    @Test
    public void testStepExecution_NegativeSleepTime() {
        step.setValue("-1000");
    }

    @Test
    public void testStepExecution_InvalidSleepValue() {
        step.setValue("invalid");
        sleepStep.step(mockWebDriver, step);
    }

    @Test
    public void testStepExecution_InterruptedException() {
        Thread.currentThread().interrupt();
        sleepStep.step(mockWebDriver, step);
        assertFalse(Thread.interrupted());
    }
}

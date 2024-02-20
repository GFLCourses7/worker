package executor.service.steps;

import executor.service.model.Step;
import executor.service.utils.StepAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class SleepTest {
    private final String SLEEP = "sleep";
    private final String TIME_SlEEP = "1000";
    private Sleep sleep;
    private WebDriver driver;


    @BeforeEach
    public void init() {
        sleep = new Sleep();
        driver = Mockito.mock(WebDriver.class);
    }

    @Test
    public void testStepAction() {
        String stepAction = sleep.getStepAction();
        assertEquals(stepAction, StepAction.SLEEP.label);
    }

    @Test
    public void testStepWithValidSleepValue() {
        Step step = new Step(SLEEP, TIME_SlEEP);
        long expectedSleepTime = 1000;
        long tolerance = 100;

        long startTime = System.currentTimeMillis();
        sleep.step(null, step);
        long endTime = System.currentTimeMillis();
        long actualSleepTime = endTime - startTime;

        assertEquals(expectedSleepTime, actualSleepTime, tolerance);
    }

    @Test
    public void testStepWithInvalidSleepValue() {
        Step step = new Step(SLEEP, "abc");
        assertThrows(NumberFormatException.class, () -> sleep.step(driver, step));
    }

    @Test
    public void testStepInterruptedException() {
        Step step = new Step(SLEEP, TIME_SlEEP);
        assertThrows(RuntimeException.class, () -> {
            doThrow(new RuntimeException()).when(sleep).step(driver, step);
            sleep.step(driver, step);
        });
    }
}

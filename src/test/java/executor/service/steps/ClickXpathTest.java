package executor.service.steps;

import executor.service.model.Step;
import executor.service.utils.StepAction;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ClickXpathTest {

    @Test
    void testGetStepAction() {
        StepExecution stepExecution = new ClickXpath();
        String stepAction = stepExecution.getStepAction();
        assertEquals(StepAction.CLICK_XPATH.label, stepAction);
    }

    @Test
    public void testStep() {
        WebDriver webDriver = mock(WebDriver.class);
        WebElement element = mock(WebElement.class);
        String xpathSelector = "//button[@id='exampleButton']";

        Step step = new Step();
        step.setValue(xpathSelector);

        when(webDriver.findElement(By.xpath(xpathSelector))).thenReturn(element);

        ClickXpath clickXpath = new ClickXpath();

        clickXpath.step(webDriver, step);

        verify(webDriver).findElement(By.xpath(xpathSelector));
        verify(element).click();
    }
}

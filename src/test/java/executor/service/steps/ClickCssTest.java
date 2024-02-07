package executor.service.steps;

import executor.service.model.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ClickCssTest {
    private Step step;
    private StepExecution stepExecution;

    @BeforeEach
    void setUp() {
        step = new Step("clickCss", "body > ul > li:nth-child(1) > a");
    }

    @Test
    void testGetStepAction() {
        // Arrange
        stepExecution = new ClickCss();
        // Act
        String stepAction = stepExecution.getStepAction();
        // Assert
        assertEquals("ClickCss", stepAction);
    }

    @Test
    void testStep() {
        // Arrange
        WebDriver mockDriver = mock(WebDriver.class);
        WebElement mockElement = mock(WebElement.class);
        stepExecution = new ClickCss();

        // Stubbing
        when(mockDriver.findElement(By.cssSelector(step.getValue()))).thenReturn(mockElement);

        // Act
        stepExecution.step(mockDriver, step);

        // Assert
        verify(mockDriver, times(1)).findElement(By.cssSelector(step.getValue()));
        verify(mockElement, times(1)).click();
    }
}
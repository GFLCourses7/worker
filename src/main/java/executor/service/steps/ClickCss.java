package executor.service.steps;

import executor.service.model.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ClickCss implements StepExecution {
    @Override
    public String getStepAction() {
        return ClickCss.class.getSimpleName();
    }

    @Override
    public void step(WebDriver webDriver, Step step) {
        String cssSelector = step.getValue();
        webDriver.findElement(By.cssSelector(cssSelector)).click();
    }
}

package executor.service;

import executor.service.model.StepDTO;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ClickCss implements StepExecution{
    @Override
    public String getStepAction() {
        return ClickCss.class.getSimpleName();
    }

    @Override
    public void step(WebDriver webDriver, StepDTO step) {
        String cssSelector = step.getValue();
        webDriver.findElement(By.cssSelector(cssSelector)).click();
    }
}

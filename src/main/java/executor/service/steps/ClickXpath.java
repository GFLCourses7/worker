package executor.service.steps;

import executor.service.model.Step;
import executor.service.utils.StepAction;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

@Service
public class ClickXpath implements StepExecution{
    @Override
    public String getStepAction() {
        return StepAction.CLICK_XPATH.label;
    }

    @Override
    public void step(WebDriver webDriver, Step step) {
        String xpathSelector = step.getValue();
        webDriver.findElement(By.xpath(xpathSelector)).click();
    }
}

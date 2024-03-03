package executor.service.factory;

import executor.service.scenario.ScenarioExecutor;
import executor.service.scenario.ScenarioExecutorService;
import executor.service.steps.StepExecutionFactoryDefault;
import executor.service.utils.WebDriverInitializer;

public class DIFactory implements AbstractFactory {

    @Override
    public <T> T create(Class<T> clazz) {

        if (ScenarioExecutor.class.isAssignableFrom(clazz))
            return (T) getScenarioExecutor();

        if (WebDriverInitializer.class.isAssignableFrom(clazz))
            return (T) getWebDriverInitializer();

        return null;
    }

    private ScenarioExecutor getScenarioExecutor() {
        return new ScenarioExecutorService(new StepExecutionFactoryDefault());
    }

    private WebDriverInitializer getWebDriverInitializer() {
        return new WebDriverInitializer();
    }
}

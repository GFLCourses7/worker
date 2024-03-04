package executor.service.factory;

import executor.service.scenario.ScenarioExecutor;
import executor.service.scenario.ScenarioExecutorService;
import executor.service.scenario.ScenarioSourceListener;
import executor.service.scenario.ScenarioSourceListenerImpl;
import executor.service.steps.StepExecutionFactoryDefault;
import executor.service.webdriver.ChromeDriverInitializer;
import executor.service.webdriver.WebDriverInitializer;

import java.util.HashMap;
import java.util.Map;

import static executor.service.factory.ContextStrategy.*;

@SuppressWarnings("unchecked")
public class DIFactory implements AbstractFactory {

    private static final Map<Class<?>, Object> context = new HashMap<>();

    @Override
    public <T> T create(Class<T> clazz) {

        if (ScenarioExecutor.class.isAssignableFrom(clazz))
            return getScenarioExecutor();

        if (WebDriverInitializer.class.isAssignableFrom(clazz))
            return getWebDriverInitializer();

        if (ScenarioSourceListener.class.isAssignableFrom(clazz))
            return getScenarioSourceListener(clazz);

        return null;
    }

    private <T> T getScenarioExecutor() {

        return (T) new ScenarioExecutorService(new StepExecutionFactoryDefault());
    }

    private <T> T getWebDriverInitializer() {

        return (T) new ChromeDriverInitializer();
    }

    private <T> T getScenarioSourceListener(Class<T> clazz) {

        return (T) SINGLETON.initWithinContext(ScenarioSourceListenerImpl::new, context, clazz);
    }

}
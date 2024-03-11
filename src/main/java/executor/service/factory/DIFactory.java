package executor.service.factory;

import executor.service.executionService.ParallelFlowExecutorService;
import executor.service.executor.ExecutionService;
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

        if (ExecutionService.class.isAssignableFrom(clazz))
            return createExecutionService(clazz);

        if (ParallelFlowExecutorService.class.isAssignableFrom(clazz))
            return getParallelFlowExecutorService(clazz);

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

    private <T> T createExecutionService(Class<T> clazz) {

        return (T) SINGLETON.initWithinContext(ExecutionService::new, context, clazz);
    }

    private <T> T getParallelFlowExecutorService(Class<T> clazz) {

        ScenarioSourceListener scenarioSourceListener = create(ScenarioSourceListener.class);
        ExecutionService executorService = create(ExecutionService.class);
        WebDriverInitializer webDriverInitializer = create(WebDriverInitializer.class);
        ScenarioExecutor scenarioExecutor = create(ScenarioExecutor.class);

        return (T) SINGLETON.initWithinContext(() -> new ParallelFlowExecutorService(
                (ScenarioSourceListenerImpl) scenarioSourceListener,
                executorService,
                webDriverInitializer,
                scenarioExecutor
        ), context, clazz);
    }

}
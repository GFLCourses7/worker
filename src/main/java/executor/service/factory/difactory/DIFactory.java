package executor.service.factory.difactory;

import executor.service.config.ChromeProxyConfigurerAddon;
import executor.service.config.proxy.ProxySourcesClientLoader;
import executor.service.executor.parallelflowexecution.ParallelFlowExecutorService;
import executor.service.executor.executionservice.ExecutionServiceImpl;
import executor.service.executor.scenarioexecutor.ScenarioExecutor;
import executor.service.executor.scenarioexecutor.ScenarioExecutorService;
import executor.service.listener.ScenarioSourceListener;
import executor.service.listener.ScenarioSourceListenerImpl;
import executor.service.factory.stepexecutionfactory.StepExecutionFactoryDefault;
import executor.service.webdriver.ChromeDriverInitializer;
import executor.service.webdriver.WebDriverInitializer;

import java.util.HashMap;
import java.util.Map;

import static executor.service.factory.difactory.ContextStrategy.*;

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

        if (ExecutionServiceImpl.class.isAssignableFrom(clazz))
            return createExecutionService(clazz);

        if (ParallelFlowExecutorService.class.isAssignableFrom(clazz))
            return getParallelFlowExecutorService(clazz);

        return null;
    }


    private <T> T getScenarioExecutor() {

        return (T) new ScenarioExecutorService(new StepExecutionFactoryDefault());
    }

    private <T> T getWebDriverInitializer() {

        return (T) new ChromeDriverInitializer(new ChromeProxyConfigurerAddon(), new ProxySourcesClientLoader());
    }

    private <T> T getScenarioSourceListener(Class<T> clazz) {

        return (T) SINGLETON.initWithinContext(ScenarioSourceListenerImpl::new, context, clazz);
    }

    private <T> T createExecutionService(Class<T> clazz) {

        return (T) SINGLETON.initWithinContext(ExecutionServiceImpl::new, context, clazz);
    }

    private <T> T getParallelFlowExecutorService(Class<T> clazz) {

        ScenarioSourceListener scenarioSourceListener = create(ScenarioSourceListener.class);
        ExecutionServiceImpl executorService = create(ExecutionServiceImpl.class);
        WebDriverInitializer webDriverInitializer = create(WebDriverInitializer.class);
        ScenarioExecutor scenarioExecutor = create(ScenarioExecutor.class);

        return (T) SINGLETON.initWithinContext(() -> new ParallelFlowExecutorService(
                scenarioSourceListener,
                executorService,
                webDriverInitializer,
                scenarioExecutor
        ), context, clazz);
    }

}
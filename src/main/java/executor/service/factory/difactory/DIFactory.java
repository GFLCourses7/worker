package executor.service.factory.difactory;

import executor.service.config.ChromeProxyConfigurer;
import executor.service.config.ChromeProxyConfigurerAddon;
import executor.service.config.ProxyConfigFileInitializer;
import executor.service.config.proxy.ProxySourcesClient;
import executor.service.config.proxy.ProxySourcesClientLoader;
import executor.service.executor.parallelflowexecution.ParallelFlowExecutorService;
import executor.service.executor.executionservice.ExecutionServiceImpl;
import executor.service.executor.scenarioexecutor.ScenarioExecutor;
import executor.service.executor.scenarioexecutor.ScenarioExecutorService;
import executor.service.factory.stepexecutionfactory.StepExecutionFactory;
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

        if (StepExecutionFactory.class.isAssignableFrom(clazz)) {

            return (T) new StepExecutionFactoryDefault();
        }

        // -------------------------------------------------------------------------------------------------------------

        if (ScenarioExecutor.class.isAssignableFrom(clazz)) {

            StepExecutionFactory stepExecutionFactory = create(StepExecutionFactory.class);

            return (T) new ScenarioExecutorService(stepExecutionFactory);
        }

        // -------------------------------------------------------------------------------------------------------------

        if (ChromeProxyConfigurer.class.isAssignableFrom(clazz)) {

            return (T) new ChromeProxyConfigurerAddon(ProxyConfigFileInitializer::new);
        }

        // -------------------------------------------------------------------------------------------------------------

        if (ProxySourcesClient.class.isAssignableFrom(clazz)) {

            return (T) SINGLETON.initWithinContext(ProxySourcesClientLoader::new, context, clazz);
        }

        // -------------------------------------------------------------------------------------------------------------

        if (WebDriverInitializer.class.isAssignableFrom(clazz)) {

            ChromeProxyConfigurer chromeProxyConfigurer = create(ChromeProxyConfigurer.class);
            ProxySourcesClient proxySourcesClient = create(ProxySourcesClient.class);

            return (T) new ChromeDriverInitializer(
                    chromeProxyConfigurer,
                    proxySourcesClient
            );
        }

        // -------------------------------------------------------------------------------------------------------------

        if (ScenarioSourceListener.class.isAssignableFrom(clazz)) {

            return (T) SINGLETON.initWithinContext(ScenarioSourceListenerImpl::new, context, clazz);
        }

        // -------------------------------------------------------------------------------------------------------------

        if (ExecutionServiceImpl.class.isAssignableFrom(clazz)) {

            return (T) SINGLETON.initWithinContext(ExecutionServiceImpl::new, context, clazz);
        }

        // -------------------------------------------------------------------------------------------------------------

        if (ParallelFlowExecutorService.class.isAssignableFrom(clazz)) {

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

        return null;
    }

}
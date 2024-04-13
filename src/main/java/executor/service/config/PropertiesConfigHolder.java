package executor.service.config;

import executor.service.model.ThreadPoolConfig;
import executor.service.model.WebDriverConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesConfigHolder {
    @Value("${executorservice.common.webDriverExecutable}")
    private String webDriverExecutable;

    @Value("${executorservice.common.userAgent}")
    private String userAgent;

    @Value("${executorservice.common.pageLoadTimeout}")
    private long pageLoadTimeout;

    @Value("${executorservice.common.driverWait}")
    private long driverWait;

    @Value("${executorservice.thread.corePoolSize}")
    private int corePoolSize;

    @Value("${executorservice.thread.keepAliveTime}")
    private long keepAliveTime;

    @Value("${executorservice.thread.maxPoolSize}")
    private int maxPoolSize;

    @Value("${executorservice.thread.queueCapacity:100}")
    private int maxQueueCapacity;

    public WebDriverConfig getWebDriverConfig() {
        return new WebDriverConfig(webDriverExecutable, userAgent, pageLoadTimeout, driverWait);
    }

    public ThreadPoolConfig getThreadPoolConfig() {
        return new ThreadPoolConfig(corePoolSize, keepAliveTime);
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public int getMaxQueueCapacity() {
        return maxQueueCapacity;
    }
}

package executor.service.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.squareup.okhttp.OkHttpClient;
import executor.service.model.ThreadPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class BeanConfig {

    private final PropertiesConfigHolder propertiesConfigHolder;

    public BeanConfig(PropertiesConfigHolder propertiesConfigHolder) {
        this.propertiesConfigHolder = propertiesConfigHolder;
    }

    @Bean
    public ObjectMapper getObjectMapperBean() {

        ObjectMapper mapper = new ObjectMapper();

        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        return mapper;
    }

    @Bean
    public OkHttpClient getOkHttpClientBean() {

        return new OkHttpClient();
    }

    @Bean
    public ThreadPoolExecutor getThreadPoolExecutorBean() {
        ThreadPoolConfig threadPoolConfig = propertiesConfigHolder.getThreadPoolConfig();

        return new ThreadPoolExecutor(
                threadPoolConfig.getCorePoolSize(),
                propertiesConfigHolder.getMaxPoolSize(),
                threadPoolConfig.getKeepAliveTime(),
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(propertiesConfigHolder.getMaxQueueCapacity())
        );
    }
}

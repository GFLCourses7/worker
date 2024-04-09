package executor.service.config.proxy;

import executor.service.model.ProxyConfigHolder;
import executor.service.service.ProxyClientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service("ProxySourcesClientOkHttp")
public class ProxySourcesClientOkHttp implements ProxySourcesClient {

    private static final Logger LOGGER = LogManager.getLogger(ProxySourcesClientOkHttp.class);

    private final ProxyClientService proxyClientService;

    public ProxySourcesClientOkHttp(ProxyClientService proxyClientService) {
        this.proxyClientService = proxyClientService;
    }

    @Override
    public ProxyConfigHolder getProxy() {

        ProxyConfigHolder proxyConfigHolder = null;

        try {
            proxyConfigHolder = proxyClientService.getProxy();
        } catch (IOException e) {
            LOGGER.error(e);
        }

        return proxyConfigHolder;
    }

    @Override
    public void addProxy(ProxyConfigHolder proxyConfigHolder) {
        // Do nothing
    }
}

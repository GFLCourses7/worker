package executor.service.config.proxy;

import executor.service.model.ProxyConfigHolder;
import executor.service.okhttp.ClientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service("ProxySourcesClientOkHttp")
public class ProxySourcesClientOkHttp implements ProxySourcesClient {

    private static final Logger LOGGER = LogManager.getLogger(ProxySourcesClientOkHttp.class);

    private final ClientService clientService;

    public ProxySourcesClientOkHttp(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public ProxyConfigHolder getProxy() {

        ProxyConfigHolder proxyConfigHolder = null;

        try {
            proxyConfigHolder = clientService.getProxy();
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

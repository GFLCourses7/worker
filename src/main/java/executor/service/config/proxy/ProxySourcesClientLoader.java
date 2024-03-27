package executor.service.config.proxy;

import executor.service.model.ProxyConfigHolder;
import executor.service.config.JsonConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class ProxySourcesClientLoader implements ProxySourcesClient {

    private static final Logger LOGGER = LogManager.getLogger(ProxySourcesClientLoader.class);
    private static final String PROXY_CONFIG_HOLDER_JSON = "ProxyConfigHolder.json";
    private final LinkedBlockingQueue<ProxyConfigHolder> proxies = new LinkedBlockingQueue<>();

    public ProxySourcesClientLoader() {
        readProxyConfigs();
    }

    private void readProxyConfigs() {

        // Look for ProxyConfigHolder.json inside /resources folder
        byte[] file = null;
        try {
            file = Objects.requireNonNull(
                    getClass().getClassLoader().getResourceAsStream(PROXY_CONFIG_HOLDER_JSON)
            ).readAllBytes();
        } catch (IOException e) {
            LOGGER.error(e);
        }

        proxies.addAll(JsonConfigReader.readFile(file, ProxyConfigHolder.class));
    }

    @Override
    public ProxyConfigHolder getProxy() {

        ProxyConfigHolder proxy = null;
        while (proxy == null) {
            try {
                proxy = proxies.take();
            } catch (InterruptedException e) {
                LOGGER.error("Interrupted while waiting for proxy", e);
                Thread.currentThread().interrupt();
            }
        }

        if (proxy.getProxyNetworkConfig() != null)
            LOGGER.info(String.format("Returning %s proxy from proxies list.", proxy.getProxyNetworkConfig().getHostname()));

        return proxy;
    }
}

package executor.service.config.proxy;

import executor.service.model.ProxyConfigHolder;
import executor.service.utils.JsonConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.NoSuchElementException;

public class ProxySourcesClientLoader implements ProxySourcesClient {

    private static final Logger LOGGER = LogManager.getLogger(ProxySourcesClientLoader.class);
    private static final String PROXY_CONFIG_HOLDER_JSON = "ProxyConfigHolder.json";
    private final List<ProxyConfigHolder> proxies;

    public ProxySourcesClientLoader() {
        proxies = readProxyConfigs();
    }

    private List<ProxyConfigHolder> readProxyConfigs() {
        return JsonConfigReader.readFile(PROXY_CONFIG_HOLDER_JSON, ProxyConfigHolder.class);
    }

    @Override
    public ProxyConfigHolder getProxy() {
        if (!proxies.isEmpty()) {
            ProxyConfigHolder proxy = proxies.remove(0);
            if (proxy.getProxyNetworkConfig() != null)
                LOGGER.info(String.format("Returning %s proxy from proxies list.", proxy.getProxyNetworkConfig().getHostname()));
            return proxy;
        }
        LOGGER.error("Trying to get proxy, while all proxies were used.");
        throw new NoSuchElementException("No proxy is available.");
    }
}

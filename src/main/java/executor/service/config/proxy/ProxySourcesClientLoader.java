package executor.service.config.proxy;

import executor.service.model.ProxyConfigHolder;
import executor.service.model.ProxyCredentials;
import executor.service.model.ProxyNetworkConfig;
import executor.service.utils.JsonConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ProxySourcesClientLoader implements ProxySourcesClient {

    private static final Logger LOGGER = LogManager.getLogger(ProxySourcesClientLoader.class);
    private static final String PROXY_NETWORK_CONFIG_JSON = "ProxyNetwork.json";
    private static final String PROXY_CREDENTIALS_JSON = "ProxyCredentials.json";
    private final List<ProxyConfigHolder> proxies;

    public ProxySourcesClientLoader() {
        proxies = readProxyConfigs();
    }

    private List<ProxyConfigHolder> readProxyConfigs() {
        LOGGER.info("Reading proxy configs.");
        List<ProxyCredentials> credentialsList =
                JsonConfigReader.readFile(PROXY_CREDENTIALS_JSON, ProxyCredentials.class);
        List<ProxyNetworkConfig> networkConfigList =
                JsonConfigReader.readFile(PROXY_NETWORK_CONFIG_JSON, ProxyNetworkConfig.class);
        List<ProxyConfigHolder> configHolderList = new ArrayList<>();
        LOGGER.info("Proxy configs were read.");
        for (int i = 0; i < Math.min(credentialsList.size(), networkConfigList.size()); i++) {
            ProxyConfigHolder holder = new ProxyConfigHolder();
            holder.setProxyNetworkConfig(networkConfigList.get(i));
            holder.setProxyCredentials(credentialsList.get(i));
            configHolderList.add(holder);
        }

        LOGGER.info("Proxy configs were loaded.");
        return configHolderList;
    }


    @Override
    public ProxyConfigHolder getProxy() {
        if (!proxies.isEmpty()) {
            ProxyConfigHolder proxy = proxies.remove(0);
            LOGGER.info("Returning proxy from proxies list.");
            return proxy;
        }
        LOGGER.error("Trying to get proxy, while all proxies were used.");
        throw new NoSuchElementException("No proxy is available.");
    }
}

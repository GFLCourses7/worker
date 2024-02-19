package executor.service.config.proxy;

import com.google.gson.reflect.TypeToken;
import executor.service.model.ProxyConfigHolder;
import executor.service.model.ProxyCredentials;
import executor.service.model.ProxyNetworkConfig;
import executor.service.utils.JsonConfigReader;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ProxySourcesClientLoader implements ProxySourcesClient {

    private static final String PROXY_NETWORK_CONFIG_JSON = "ProxyNetwork.json";
    private static final String PROXY_CREDENTIALS_JSON = "ProxyCredentials.json";
    private final List<ProxyConfigHolder> proxies;

    public ProxySourcesClientLoader() {
        proxies = readProxyConfigs();
    }

    private List<ProxyConfigHolder> readProxyConfigs() {
        List<ProxyCredentials> credentialsList = JsonConfigReader.readFile(PROXY_CREDENTIALS_JSON,
                new TypeToken<List<ProxyCredentials>>() {
                }
                        .getType());
        List<ProxyNetworkConfig> networkConfigList = JsonConfigReader.readFile(PROXY_NETWORK_CONFIG_JSON,
                new TypeToken<List<ProxyNetworkConfig>>() {
                }
                        .getType());

        List<ProxyConfigHolder> configHolderList = new ArrayList<>();

        for (int i = 0; i < Math.min(credentialsList.size(), networkConfigList.size()); i++) {
            ProxyConfigHolder holder = new ProxyConfigHolder();
            holder.setProxyNetworkConfig(networkConfigList.get(i));
            holder.setProxyCredentials(credentialsList.get(i));
            configHolderList.add(holder);
        }
        return configHolderList;
    }


    @Override
    public ProxyConfigHolder getProxy() {
        if (!proxies.isEmpty()) {
            return proxies.remove(0);
        }
        throw new NoSuchElementException("No proxy is available.");
    }
}

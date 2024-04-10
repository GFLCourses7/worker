package executor.service.config.proxy;

import executor.service.model.ProxyConfigHolder;
import executor.service.utils.configreader.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingQueue;


@Service
public class ProxySourcesClientLoader implements ProxySourcesClient {

    private static final Logger LOGGER = LogManager.getLogger(ProxySourcesClientLoader.class);
    //        private static final String PROXY_CONFIG_HOLDER_JSON = "ProxyConfigHolder.json.empty";
    private static final String PROXY_CONFIG_HOLDER_JSON = "ProxyConfigHolder.json";
    private final LinkedBlockingQueue<ProxyConfigHolder> proxies = new LinkedBlockingQueue<>();
    private final ConfigReader configReader;

    public ProxySourcesClientLoader(ConfigReader configReader) {
        this.configReader = configReader;
        proxies.addAll(configReader.readFile(PROXY_CONFIG_HOLDER_JSON, ProxyConfigHolder.class));
    }

    @Override
    public ProxyConfigHolder getProxy() {
        return proxies.poll();
    }


    @Override
    public void addProxy(ProxyConfigHolder proxyConfigHolder) {
        proxies.add(proxyConfigHolder);
    }
}

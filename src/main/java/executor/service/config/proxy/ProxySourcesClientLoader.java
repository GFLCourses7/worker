package executor.service.config.proxy;

import executor.service.config.JsonConfigReader;
import executor.service.model.ProxyConfigHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.LinkedBlockingQueue;


@Service
public class ProxySourcesClientLoader implements ProxySourcesClient {

    private static final Logger LOGGER = LogManager.getLogger(ProxySourcesClientLoader.class);
    //    private static final String PROXY_CONFIG_HOLDER_JSON = "ProxyConfigHolder.json.empty";
    private static final String PROXY_CONFIG_HOLDER_JSON = "ProxyConfigHolder.json";
    private final LinkedBlockingQueue<ProxyConfigHolder> proxies = new LinkedBlockingQueue<>();

    public ProxySourcesClientLoader() {
        readProxyConfigs();
    }

    private void readProxyConfigs() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROXY_CONFIG_HOLDER_JSON)) {
            if (inputStream != null) {
                byte[] file = inputStream.readAllBytes();
                proxies.addAll(JsonConfigReader.readFile(file, ProxyConfigHolder.class));
            } else {
                LOGGER.warn(PROXY_CONFIG_HOLDER_JSON + " not found in resources folder.");
            }
        } catch (IOException e) {
            LOGGER.error("Error reading ProxyConfigHolder.json", e);
        }
    }

    @Override
    public ProxyConfigHolder getProxy() {
        return proxies.poll();

//        ProxyConfigHolder proxy = null;
//        try {
//            proxy = proxies.take();
//            if (proxy.getProxyNetworkConfig() != null) {
//                LOGGER.info(String.format("Returning %s proxy from proxies list.", proxy.getProxyNetworkConfig().getHostname()));
//            }
//        } catch (InterruptedException e) {
//            LOGGER.error("Interrupted while waiting for proxy", e);
//            Thread.currentThread().interrupt();
//        }
//        return proxy;
    }


    @Override
    public void addProxy(ProxyConfigHolder proxyConfigHolder) {
        proxies.add(proxyConfigHolder);
    }
}

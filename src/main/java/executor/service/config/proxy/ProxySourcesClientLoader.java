package executor.service.config.proxy;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import executor.service.model.ProxyConfigHolder;
import executor.service.config.JsonConfigReader;
import executor.service.model.ProxyConfigHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.io.InputStream;
import java.util.concurrent.LinkedBlockingQueue;


@Service
public class ProxySourcesClientLoader implements ProxySourcesClient {

    private static final Logger LOGGER = LogManager.getLogger(ProxySourcesClientLoader.class);
    //    private static final String PROXY_CONFIG_HOLDER_JSON = "ProxyConfigHolder.json.empty";
    private static final String PROXY_CONFIG_HOLDER_JSON = "ProxyConfigHolder.json";
    private final Queue<ProxyConfigHolder> proxies = new LinkedBlockingQueue<>();
    private final OkHttpClient client = new OkHttpClient();
    // TODO: Rename using Client-Server url, move to props.
    private final String clientServiceUrl = "http://client-service-url";


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

        fetchProxies();

        // TODO: null returning is bad practice
        ProxyConfigHolder proxy = proxies.poll();

        if (proxy != null) {
            LOGGER.info(String.format("Returning %s proxy from proxies list.", proxy.getProxyNetworkConfig().getHostname()));
        } else {
            LOGGER.info("Queue of proxy is empty.");
        }
        return proxy;
    }

    public void fetchProxies() {
        Request request = new Request.Builder()
                .url(clientServiceUrl + "/get-all-proxy")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }
            String responseBody = response.body().string();
            byte[] jsonData = responseBody.getBytes(StandardCharsets.UTF_8);

            List<ProxyConfigHolder> proxyList = JsonConfigReader.readFile(jsonData, ProxyConfigHolder.class);

            proxies.addAll(proxyList);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
  
    @Override
    public void addProxy(ProxyConfigHolder proxyConfigHolder) {
        proxies.add(proxyConfigHolder);
    }
}

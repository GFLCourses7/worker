package executor.service.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import executor.service.model.ProxyConfigHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProxyClientService {

    private static final Logger LOGGER = LogManager.getLogger(ProxyClientService.class);

    private final String host;
    private final Integer port;
    protected final String api = "/internal/get-proxy";

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper;

    public ProxyClientService(
            @Value("${executorservice.client.host}") String host,
            @Value("${executorservice.client.port}") Integer port,
            ObjectMapper objectMapper
    ) {
        this.host = host;
        this.port = port;
        this.objectMapper = objectMapper;

        setupObjectMapper();

        try {
            getProxy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupObjectMapper() {

        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public ProxyConfigHolder getProxy() throws IOException {

        String url = String.format("%s:%s%s", host, port, api);

        LOGGER.info("Fetching proxy from " + url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();

        return objectMapper.readValue(
                response.body().string().getBytes(StandardCharsets.UTF_8),
                ProxyConfigHolder.class
        );
    }
}

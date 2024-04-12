package executor.service.okhttp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;
import executor.service.model.ProxyConfigHolder;
import executor.service.model.Scenario;
import executor.service.model.ScenarioWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class ClientService {

    private final Logger LOGGER = LogManager.getLogger(ClientService.class);

    private final String CLIENT_HOST;
    private final Integer CLIENT_PORT;
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public ClientService(
            @Value("${executorservice.client.host}") String client_host,
            @Value("${executorservice.client.port}") Integer client_port,
            OkHttpClient client,
            ObjectMapper objectMapper
    ) {
        CLIENT_HOST = client_host;
        CLIENT_PORT = client_port;
        this.client = client;
        this.objectMapper = objectMapper;
    }

    public Scenario fetchScenario() {

        String api = "/internal/get-scenario";
        String url = String.format("%s:%s%s", CLIENT_HOST, CLIENT_PORT, api);

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }
            String responseBody = response.body().string();
            return objectMapper.readValue(responseBody, objectMapper.getTypeFactory().constructType(ScenarioWrapper.class));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ProxyConfigHolder getProxy() throws IOException {

        String api = "/internal/get-proxy";
        String url = String.format("%s:%s%s", CLIENT_HOST, CLIENT_PORT, api);

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

    public void sendResult(ScenarioWrapper scenario) throws IOException {

        String api = "/internal/set-result";
        String url = String.format("%s:%s%s", CLIENT_HOST, CLIENT_PORT, api);

        LOGGER.info("Sending result to client: " + url);

        String result = objectMapper.writeValueAsString(scenario);

        Request request = new Request.Builder()
                .url(url)
                .method("POST", RequestBody.create(MediaType.parse("application/json"), result))
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();

        LOGGER.info("Set result response: " + response.toString());
    }
}

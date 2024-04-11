package executor.service.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;
import executor.service.model.ScenarioWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ResultClientService {

    private static final Logger LOGGER = LogManager.getLogger(ResultClientService.class);

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String host;
    private final Integer port;
    protected final String api = "/internal/set-result";

    public ResultClientService(
            @Value("${executorservice.client.host}") String host,
            @Value("${executorservice.client.port}") Integer port
    ) {
        this.host = host;
        this.port = port;

        setupObjectMapper();
    }

    private void setupObjectMapper() {

        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public void sendResult(ScenarioWrapper scenario) throws IOException {

        String url = String.format("%s:%s%s", host, port, api);

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

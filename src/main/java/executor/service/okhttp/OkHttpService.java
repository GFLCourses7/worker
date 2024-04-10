package executor.service.okhttp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import executor.service.model.Scenario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OkHttpService extends OkHttpClient {

    @Value("${client.host}")
    private String CLIENT_HOST;

    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public OkHttpService(OkHttpClient client, ObjectMapper objectMapper) {
        this.client = client;
        this.objectMapper = objectMapper;
    }

    public Scenario fetchScenario() {
        Request request = new Request.Builder()
                .url(CLIENT_HOST + "/get-scenario")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }
            String responseBody = response.body().string();
            return objectMapper.readValue(responseBody, objectMapper.getTypeFactory().constructType(Scenario.class));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

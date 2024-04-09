package executor.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import executor.service.config.proxy.ProxySourcesClientLoader;
import executor.service.model.ProxyConfigHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import executor.service.model.ProxyCredentials;
import executor.service.model.ProxyNetworkConfig;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProxyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProxySourcesClientLoader proxySourcesClientLoader;

    @InjectMocks
    private ProxyController proxyController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(proxyController).build();
    }

    @Test
    public void testAddProxy() throws Exception {
        ProxyConfigHolder proxyConfigHolder = new ProxyConfigHolder(
                new ProxyNetworkConfig("example.com", 8080),
                new ProxyCredentials("username", "password")
        );

        ObjectMapper objectMapper = new ObjectMapper();
        String proxyJson = objectMapper.writeValueAsString(proxyConfigHolder);

        // Mocking the behavior of proxySourcesClientLoader.addProxy(proxyConfigHolder)
        doNothing().when(proxySourcesClientLoader).addProxy(any(ProxyConfigHolder.class));

        mockMvc.perform(post("/add-proxy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(proxyJson))
                .andExpect(status().isOk());

        verify(proxySourcesClientLoader).addProxy(proxyConfigHolder);
    }
}

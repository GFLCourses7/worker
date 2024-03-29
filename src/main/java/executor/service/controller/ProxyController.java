package executor.service.controller;

import executor.service.config.proxy.ProxySourcesClientLoader;
import executor.service.model.ProxyConfigHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/proxy")
public class ProxyController {

    private final ProxySourcesClientLoader proxySourcesClientLoader;

    public ProxyController(ProxySourcesClientLoader scenarioSourceListener) {
        this.proxySourcesClientLoader = scenarioSourceListener;
    }

    @PostMapping
    ProxyConfigHolder addScenario(@RequestBody ProxyConfigHolder proxy) {
        proxySourcesClientLoader.addProxy(proxy);
        return proxy;
    }
}

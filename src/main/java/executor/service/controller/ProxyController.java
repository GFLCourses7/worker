package executor.service.controller;

import executor.service.config.proxy.ProxySourcesClientLoader;
import executor.service.model.ProxyConfigHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProxyController {

    private final ProxySourcesClientLoader proxySourcesClientLoader;

    public ProxyController(ProxySourcesClientLoader proxySourcesClientLoader) {
        this.proxySourcesClientLoader = proxySourcesClientLoader;
    }

    @PostMapping("/add-proxy")
    ProxyConfigHolder addProxy(@RequestBody ProxyConfigHolder proxy) {
        proxySourcesClientLoader.addProxy(proxy);
        return proxy;
    }
}

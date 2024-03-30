package executor.service.config.proxy;

import executor.service.model.ProxyConfigHolder;

public interface ProxySourcesClient{
    ProxyConfigHolder getProxy();
    void addProxy(ProxyConfigHolder proxyConfigHolder);
}

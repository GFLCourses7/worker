package executor.service.config.proxy;

import executor.service.model.ProxyConfigHolder;
import org.springframework.beans.factory.annotation.Qualifier;


public interface ProxySourcesClient{
    ProxyConfigHolder getProxy();
    void addProxy(ProxyConfigHolder proxyConfigHolder);
}

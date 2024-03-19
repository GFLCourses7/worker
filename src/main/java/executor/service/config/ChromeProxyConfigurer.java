package executor.service.config;

import executor.service.model.ProxyConfigHolder;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;

public interface ChromeProxyConfigurer {
    Runnable configureProxy(ChromeOptions options, ProxyConfigHolder proxyConfigHolder) throws IOException;
}

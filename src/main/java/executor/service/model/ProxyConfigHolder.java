package executor.service.model;

import java.util.Objects;

public class ProxyConfigHolder {
    private ProxyNetworkConfig proxyNetworkConfig;
    private ProxyCredentials proxyCredentials;


    public ProxyConfigHolder() {
        proxyNetworkConfig = new ProxyNetworkConfig();
        proxyCredentials = new ProxyCredentials();
    }

    public ProxyConfigHolder(ProxyNetworkConfig proxyNetworkConfig, ProxyCredentials proxyCredentials) {
        this.proxyNetworkConfig = proxyNetworkConfig;
        this.proxyCredentials = proxyCredentials;
    }

    public ProxyNetworkConfig getProxyNetworkConfig() {
        return proxyNetworkConfig;
    }

    public void setProxyNetworkConfig(ProxyNetworkConfig proxyNetworkConfig) {
        this.proxyNetworkConfig = proxyNetworkConfig;
    }

    public ProxyCredentials getProxyCredentials() {
        return proxyCredentials;
    }

    public void setProxyCredentials(ProxyCredentials proxyCredentials) {
        this.proxyCredentials = proxyCredentials;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProxyConfigHolder that = (ProxyConfigHolder) o;
        return Objects.equals(proxyNetworkConfig, that.proxyNetworkConfig) && Objects.equals(proxyCredentials, that.proxyCredentials);
    }

    @Override
    public int hashCode() {
        return Objects.hash(proxyNetworkConfig, proxyCredentials);
    }

    @Override
    public String toString() {
        return "ProxyConfigHolder{" +
                "proxyNetworkConfig=" + proxyNetworkConfig +
                ", proxyCredentials=" + proxyCredentials +
                '}';
    }
}

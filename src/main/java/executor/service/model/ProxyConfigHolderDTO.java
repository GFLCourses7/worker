package executor.service.model;

import java.util.Objects;

public class ProxyConfigHolderDTO {
    private ProxyNetworkConfigDTO proxyNetworkConfig;
    private ProxyCredentialsDTO proxyCredentials;


    public ProxyConfigHolderDTO() {
    }

    public ProxyConfigHolderDTO(ProxyNetworkConfigDTO proxyNetworkConfig, ProxyCredentialsDTO proxyCredentials) {
        this.proxyNetworkConfig = proxyNetworkConfig;
        this.proxyCredentials = proxyCredentials;
    }

    public ProxyNetworkConfigDTO getProxyNetworkConfig() {
        return proxyNetworkConfig;
    }

    public void setProxyNetworkConfig(ProxyNetworkConfigDTO proxyNetworkConfig) {
        this.proxyNetworkConfig = proxyNetworkConfig;
    }

    public ProxyCredentialsDTO getProxyCredentials() {
        return proxyCredentials;
    }

    public void setProxyCredentials(ProxyCredentialsDTO proxyCredentials) {
        this.proxyCredentials = proxyCredentials;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProxyConfigHolderDTO that = (ProxyConfigHolderDTO) o;
        return Objects.equals(proxyNetworkConfig, that.proxyNetworkConfig) && Objects.equals(proxyCredentials, that.proxyCredentials);
    }

    @Override
    public int hashCode() {
        return Objects.hash(proxyNetworkConfig, proxyCredentials);
    }
}

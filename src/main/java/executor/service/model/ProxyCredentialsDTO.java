package executor.service.model;

import java.util.Objects;

public class ProxyCredentialsDTO {
    private String username;
    private String password;

    public ProxyCredentialsDTO() {
    }

    public ProxyCredentialsDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ProxyCredentialsDTO proxy = (ProxyCredentialsDTO) obj;
        return Objects.equals(username, proxy.username) && Objects.equals(password, proxy.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}

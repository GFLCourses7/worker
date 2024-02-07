package executor.service.model;

import java.util.Objects;

public class WebDriverConfig {
    String webDriverExecutable;
    String userAgent;
    Long pageLoadTimeout;
    Long implicitlyWait;

    public WebDriverConfig() {
    }

    public WebDriverConfig(String webDriverExecutable, String userAgent, Long pageLoadTimeout, Long implicitlyWait) {
        this.webDriverExecutable = webDriverExecutable;
        this.userAgent = userAgent;
        this.pageLoadTimeout = pageLoadTimeout;
        this.implicitlyWait = implicitlyWait;
    }

    public String getWebDriverExecutable() {
        return webDriverExecutable;
    }

    public void setWebDriverExecutable(String webDriverExecutable) {
        this.webDriverExecutable = webDriverExecutable;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Long getPageLoadTimeout() {
        return pageLoadTimeout;
    }

    public void setPageLoadTimeout(Long pageLoadTimeout) {
        this.pageLoadTimeout = pageLoadTimeout;
    }

    public Long getImplicitlyWait() {
        return implicitlyWait;
    }

    public void setImplicitlyWait(Long implicitlyWait) {
        this.implicitlyWait = implicitlyWait;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WebDriverConfig)) return false;
        WebDriverConfig that = (WebDriverConfig) o;
        return Objects.equals(getWebDriverExecutable(), that.getWebDriverExecutable()) && Objects.equals(getUserAgent(), that.getUserAgent()) && Objects.equals(getPageLoadTimeout(), that.getPageLoadTimeout()) && Objects.equals(getImplicitlyWait(), that.getImplicitlyWait());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWebDriverExecutable(), getUserAgent(), getPageLoadTimeout(), getImplicitlyWait());
    }
}

package executor.service.model;

import java.util.Objects;

public class ThreadPoolConfigDTO {
    private Integer corePoolSize;
    private Long keepAliveTime;

    public ThreadPoolConfigDTO() {
    }

    public ThreadPoolConfigDTO(Integer corePoolSize, Long keepAliveTime) {
        this.corePoolSize = corePoolSize;
        this.keepAliveTime = keepAliveTime;
    }

    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public Long getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(Long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThreadPoolConfigDTO that = (ThreadPoolConfigDTO) o;
        return Objects.equals(corePoolSize, that.corePoolSize) && Objects.equals(keepAliveTime, that.keepAliveTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(corePoolSize, keepAliveTime);
    }
}

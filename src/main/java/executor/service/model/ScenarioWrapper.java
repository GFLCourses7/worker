package executor.service.model;

import java.util.List;
import java.util.Objects;

public class ScenarioWrapper extends Scenario {

    private Long id;
    private String result;

    public ScenarioWrapper() {
    }

    public ScenarioWrapper(Long id, String result) {
        this.id = id;
        this.result = result;
    }

    public ScenarioWrapper(String name, String site, List<Step> steps, Long id, String result) {
        super(name, site, steps);
        this.id = id;
        this.result = result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ScenarioWrapper that = (ScenarioWrapper) o;
        return Objects.equals(id, that.id) && Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, result);
    }

    @Override
    public String toString() {
        return "ScenarioWrapper{" +
                "name='" + name + '\'' +
                ", site='" + site + '\'' +
                ", steps=" + steps +
                ", id=" + id +
                ", result='" + result + '\'' +
                '}';
    }
}

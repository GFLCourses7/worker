package executor.service.utils;

public enum StepAction {
    CLICK_CSS("clickCss");

    public final String label;

    StepAction(String label) {
        this.label = label;
    }
}

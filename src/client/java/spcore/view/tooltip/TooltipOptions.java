package spcore.view.tooltip;

import spcore.view.render.ViewOptions;

public class TooltipOptions extends ViewOptions {
    private int tooltipDelay;

    public int getTooltipDelay() {
        return tooltipDelay;
    }

    public void setTooltipDelay(int tooltipDelay) {
        this.tooltipDelay = tooltipDelay;
        super.change();
    }
}

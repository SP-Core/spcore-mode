package spcore.view.render;

import spcore.view.tooltip.TooltipOptions;

public class ComponentsOptions {

    public final TooltipOptions tooltip;

    public ComponentsOptions() {
        this.tooltip = new TooltipOptions();
        this.tooltip.setTooltipDelay(20);
    }

    public <T extends ViewOptions> T getValue(T def, T custom){
        if(custom.isDefault()){
            return def;
        }

        return custom;
    }

    public int getTooltipDelay() {
        return tooltip.getTooltipDelay();
    }

    public ComponentsOptions setTooltipDelay(int tooltipDelay) {
        tooltip.setTooltipDelay(tooltipDelay);
        return this;
    }
}

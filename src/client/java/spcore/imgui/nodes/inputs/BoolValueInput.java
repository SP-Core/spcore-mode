package spcore.imgui.nodes.inputs;

import imgui.ImGui;
import imgui.type.ImBoolean;
import imgui.type.ImFloat;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.Pin;

public class BoolValueInput extends AbstractValueInput{
    @Override
    public void render(Node node, Pin pin) {
        ImBoolean value;
        if(node.values.containsKey(pin.id.value)){
            value = new ImBoolean(Boolean.parseBoolean(node.values.get(pin.id.value)));
        }
        else{
            value = new ImBoolean(false);
        }
        ImGui.pushItemWidth(200);
        if(ImGui.checkbox(pin.pinName, value)){
            node.values.put(pin.id.value, Boolean.toString(value.get()));
        }
    }
}

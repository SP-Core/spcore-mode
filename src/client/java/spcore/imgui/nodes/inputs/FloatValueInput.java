package spcore.imgui.nodes.inputs;

import imgui.ImGui;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.Pin;

public class FloatValueInput extends AbstractValueInput{
    @Override
    public void render(Node node, Pin pin) {
        ImFloat value;
        if(node.values.containsKey(pin.id.value)){
            value = new ImFloat(Float.parseFloat(node.values.get(pin.id.value)));
        }
        else{
            value = new ImFloat(0);
        }
        ImGui.pushItemWidth(200);
        if(ImGui.inputFloat(pin.name, value)){
            node.values.put(pin.id.value, value.toString());
        }
    }
}

package spcore.imgui.nodes.inputs;

import imgui.ImGui;
import imgui.type.ImInt;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.Pin;

public class IntValueInput extends AbstractValueInput {

    @Override
    public void render(Node node, Pin pin) {
        ImInt value;
        if(node.values.containsKey(pin.id.value)){
            value = new ImInt(Integer.parseInt(node.values.get(pin.id.value)));
        }
        else{
            value = new ImInt(0);
        }
        ImGui.pushID(pin.id.toString());
        ImGui.pushItemWidth(200);
        if(ImGui.inputInt(pin.name, value)){
            node.values.put(pin.id.value, value.toString());
        }
        ImGui.popID();

    }
}

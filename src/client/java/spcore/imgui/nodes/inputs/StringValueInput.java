package spcore.imgui.nodes.inputs;

import imgui.ImGui;
import imgui.ImGuiInputTextCallbackData;
import imgui.callback.ImGuiInputTextCallback;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImString;
import spcore.GlobalContext;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.Pin;
import spcore.imgui.nodes.models.PinId;

import java.util.HashMap;

public class StringValueInput extends AbstractValueInput {

    private final HashMap<PinId, ImString> values = new HashMap<>();
    @Override
    public void render(Node node, Pin pin) {
        ImString value;
        if(values.containsKey(pin.id)){
            value = values.get(pin.id);
        }
        else{
            value = new ImString(node.values.getOrDefault(pin.id.value, ""), 200);
            values.put(pin.id, value);
        }
        ImGui.pushItemWidth(200);
        ImGui.pushID(pin.id.toString());
        ImGui.inputText("##" + pin.id.toString(), value,
                ImGuiInputTextFlags.CallbackAlways,
                new ImGuiTextCallback(node, pin));

        ImGui.popID();
    }

    public static class ImGuiTextCallback extends ImGuiInputTextCallback {

        private final Node node;
        private final Pin pin;

        public ImGuiTextCallback(Node node, Pin pin) {
            this.node = node;
            this.pin = pin;
        }

        @Override
        public void accept(ImGuiInputTextCallbackData imGuiInputTextCallbackData) {
            node.values.put(pin.id.value, imGuiInputTextCallbackData.getBuf());
        }
    }
}

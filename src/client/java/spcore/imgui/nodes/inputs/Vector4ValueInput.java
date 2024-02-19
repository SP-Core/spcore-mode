package spcore.imgui.nodes.inputs;

import imgui.ImGui;
import imgui.type.ImInt;
import org.joml.Vector4f;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.Pin;

public class Vector4ValueInput extends AbstractValueInput{
    @Override
    public void render(Node node, Pin pin) {
        float[] vector = new float[4];
        if(node.values.containsKey(pin.id.value)){
            var v = parse(node.values.get(pin.id.value));
            vector[0] = v.x;
            vector[1] = v.y;
            vector[2] = v.z;
            vector[3] = v.w;
        }

        ImGui.pushID(pin.id.toString());
        ImGui.pushItemWidth(200);
        if(ImGui.colorPicker4("", vector)){
            node.values.put(pin.id.value, convert(vector));
        }
        ImGui.popID();
    }

    public static Vector4f parse(String value){
        var nodes = value.split(";");

        return new Vector4f(Float.parseFloat(nodes[0]),
                Float.parseFloat(nodes[1]),
                Float.parseFloat(nodes[2]),
                Float.parseFloat(nodes[3]));

    }

    public static String convert(float[] vector){
        return vector[0] + ";" + vector[1] + ";" + vector[2] + ";" + vector[3];

    }
}

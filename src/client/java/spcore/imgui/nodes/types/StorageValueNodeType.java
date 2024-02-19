package spcore.imgui.nodes.types;

import imgui.ImColor;
import spcore.imgui.nodes.NodeContext;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.Pin;
import spcore.imgui.nodes.processor.ProcessService;

import java.util.HashMap;

public class StorageValueNodeType extends AbstractNodeType {


    @Override
    public String getName() {
        return "Storage";
    }

    @Override
    public Node create(NodeContext context) {
        var node = new Node(context.nextId(), getName(), ImColor.floatToColor(255, 128, 128));

        node.inputs.add(
                new Pin(context.nextId(),
                        "int",
                        PinType.Int));

        node.inputs.add(
                new Pin(context.nextId(),
                        "float",
                        PinType.Float));

        node.outputs.add(
                new Pin(context.nextId(),
                        "int",
                        PinType.Int));

        node.outputs.add(
                new Pin(context.nextId(),
                        "float",
                        PinType.Float));

        return node;
    }

    @Override
    public HashMap<String, Object> process(Node node, ProcessService inputs) {
        if(inputs.data.statics.containsKey(node.id)){
            return inputs.data.statics.get(node.id);
        }

        HashMap<String, Object> outputs = new HashMap<>();

        if(inputs.contains("int")){
            outputs.put("int", inputs.process("int"));
        }
        else if(node.containsInputValue("int")){
            outputs.put("int", Integer.parseInt(node.getInputValue("int")));
        }

        if(inputs.contains("float")){
            outputs.put("float", inputs.process("float"));
        }
        else if(node.containsInputValue("float")){
            outputs.put("float", Integer.parseInt(node.getInputValue("float")));
        }

        inputs.data.statics.put(node.id, outputs);
        return outputs;
    }
}

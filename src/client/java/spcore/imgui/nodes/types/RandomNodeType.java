package spcore.imgui.nodes.types;

import imgui.ImColor;
import org.joml.Vector2f;
import spcore.imgui.nodes.NodeContext;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.Pin;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.view.ViewComponent;
import spcore.view.render.Renderable;

import java.util.HashMap;
import java.util.Random;

public class RandomNodeType extends AbstractNodeType{
    @Override
    public String getName() {
        return "Random";
    }

    @Override
    public Node create(NodeContext context) {
        var node = new Node(context.nextId(), getName(), ImColor.floatToColor(255, 128, 128));

        node.inputs.add(
                new Pin(context.nextId(),
                        "seed",
                        PinType.Int));

        node.inputs.add(
                new Pin(context.nextId(),
                        "min",
                        PinType.Int));

        node.inputs.add(
                new Pin(context.nextId(),
                        "max",
                        PinType.Int));

        node.outputs.add(
                new Pin(context.nextId(),
                        "vector2",
                        PinType.Vector2));
        node.outputs.add(
                new Pin(context.nextId(),
                        "vector3",
                        PinType.Vector3));
        node.outputs.add(
                new Pin(context.nextId(),
                        "vector4",
                        PinType.Vector4));

        node.outputs.add(
                new Pin(context.nextId(),
                        "int",
                        PinType.Int));

        node.outputs.add(
                new Pin(context.nextId(),
                        "float",
                        PinType.Float));

        context.nodes.add(node);
        return node;
    }

    @Override
    public HashMap<String, Object> process(Node node, ProcessService inputs) {
        HashMap<String, Object> outputs = new HashMap<>();

        int seed = 1;

        if(inputs.contains("seed")){
            seed = (int)inputs.process("seed");
        }
        else if(node.containsInputValue("seed")){
            seed = Integer.parseInt(node.getInputValue("seed"));
        }


        int min = 1;
        if(inputs.contains("min")){
            min = (int)inputs.process("min");
        }
        else if(node.containsInputValue("min")){
            min = Integer.parseInt(node.getInputValue("min"));
        }

        int max = 1;
        if(inputs.contains("max")){
            max = (int)inputs.process("max");
        }
        else if(node.containsInputValue("max")){
            max = Integer.parseInt(node.getInputValue("max"));
        }

        Random rd;
        if(inputs.data.randoms.containsKey(node.id)){
            rd = inputs.data.randoms.get(node.id);
        }
        else{
            rd = new Random(seed);
            inputs.data.randoms.put(node.id, rd);
        }

        outputs.put("vector2", new Vector2f(rd.nextFloat(), rd.nextFloat()));
        outputs.put("vector3", new Vector2f(rd.nextFloat(), rd.nextFloat()));
        outputs.put("vector4", new Vector2f(rd.nextFloat(), rd.nextFloat()));
        outputs.put("int", rd.nextInt((max - min) + 1) + min);
        outputs.put("float", rd.nextFloat());

        return outputs;
    }
}

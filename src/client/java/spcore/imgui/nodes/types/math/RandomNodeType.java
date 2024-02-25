package spcore.imgui.nodes.types.math;

import imgui.ImColor;
import org.joml.Vector2f;
import spcore.imgui.nodes.NodeContext;
import spcore.imgui.nodes.enums.NodeType;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.NodeInfo;
import spcore.imgui.nodes.models.Pin;
import spcore.imgui.nodes.models.PinInfo;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.imgui.nodes.types.AbstractNodeType;
import spcore.view.ViewComponent;
import spcore.view.render.Renderable;

import java.util.HashMap;
import java.util.Random;

public class RandomNodeType extends AbstractNodeType {

    @Override
    public NodeInfo internalCreateInfo(NodeType nt) {
        var node = new NodeInfo(nt);
        node.addInput(new PinInfo("seed", PinType.Int));
        node.addInput(new PinInfo("min", PinType.Int));
        node.addInput(new PinInfo("max", PinType.Int));

        node.addOutput(new PinInfo("vector2", PinType.Vector2));
        node.addOutput(new PinInfo("vector3", PinType.Vector3));
        node.addOutput(new PinInfo("vector4", PinType.Vector4));
        node.addOutput(new PinInfo("int", PinType.Int));
        node.addOutput(new PinInfo("float", PinType.Float));
        return node;
    }


    @Override
    public HashMap<String, Object> internalProcess(Node node, ProcessService inputs) {
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
        if((max - min) + 1 > 0){
            outputs.put("int", rd.nextInt((max - min) + 1) + min);
        }
        else{
            outputs.put("int", 0);
        }
        outputs.put("float", rd.nextFloat());

        return outputs;
    }
}

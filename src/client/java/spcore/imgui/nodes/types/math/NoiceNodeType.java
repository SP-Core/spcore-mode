//package spcore.imgui.nodes.types.math;
//
//import imgui.ImColor;
//import org.joml.Vector2f;
//import spcore.imgui.nodes.NodeContext;
//import spcore.imgui.nodes.enums.PinType;
//import spcore.imgui.nodes.models.Node;
//import spcore.imgui.nodes.models.Pin;
//import spcore.imgui.nodes.processor.ProcessService;
//import spcore.imgui.nodes.types.AbstractNodeType;
//
//import java.util.HashMap;
//import java.util.Random;
//
//public class NoiceNodeType extends AbstractNodeType {
//    @Override
//    public String getName() {
//        return "Noice";
//    }
//
//    @Override
//    public Node create(NodeContext context) {
//        var node = new Node(context.nextId(), getName(), ImColor.floatToColor(255, 128, 128));
//
//        node.inputs.add(
//                new Pin(context.nextId(),
//                        "vector",
//                        PinType.Vector2));
//
//        node.inputs.add(
//                new Pin(context.nextId(),
//                        "seed",
//                        PinType.Int));
//
//        node.inputs.add(
//                new Pin(context.nextId(),
//                        "scale",
//                        PinType.Float));
//
//        node.inputs.add(
//                new Pin(context.nextId(),
//                        "detail",
//                        PinType.Int));
//
//        node.inputs.add(
//                new Pin(context.nextId(),
//                        "roughness",
//                        PinType.Float));
//
//        node.inputs.add(
//                new Pin(context.nextId(),
//                        "lacunarity",
//                        PinType.Float));
//
//        node.outputs.add(
//                new Pin(context.nextId(),
//                        "fac",
//                        PinType.Float));
//
//        return node;
//    }
//
//    @Override
//    public HashMap<String, Object> process(Node node, ProcessService inputs) {
//
//        Vector2f vector;
//        if(inputs.contains("vector")){
//            vector = (Vector2f)inputs.process("vector");
//        }
//        else{
//            var rd = new Random();
//            vector = new Vector2f(rd.nextFloat(), rd.nextFloat());
//        }
//
//        int seed = 1;
//        if(inputs.contains("seed")){
//            seed = (int)inputs.process("seed");
//        }
//        else if(node.containsInputValue("seed")){
//            seed = Integer.parseInt(node.getInputValue("seed"));
//        }
//        var perlin = new Perlin2D(seed);
//
//        float scale = 1;
//        if(inputs.contains("scale")){
//            scale = (float)inputs.process("scale");
//        }
//        else if(node.containsInputValue("scale")){
//            scale = Float.parseFloat(node.getInputValue("scale"));
//        }
//
//        int detail = 1;
//        if(inputs.contains("detail")){
//            detail = (int)inputs.process("detail");
//        }
//        else if(node.containsInputValue("detail")){
//            detail = Integer.parseInt(node.getInputValue("detail"));
//        }
//
//        float roughness = 0.5f;
//        if(inputs.contains("roughness")){
//            roughness = (float)inputs.process("roughness");
//        }
//        else if(node.containsInputValue("roughness")){
//            roughness = Float.parseFloat(node.getInputValue("roughness"));
//        }
//
//        float lacunarity = 0f;
//        if(inputs.contains("lacunarity")){
//            lacunarity = (float)inputs.process("lacunarity");
//        }
//        else if(node.containsInputValue("lacunarity")){
//            lacunarity = Float.parseFloat(node.getInputValue("lacunarity"));
//        }
////        var v = perlin.noise(vector.x, vector.y, scale, detail, roughness, lacunarity);
//        HashMap<String, Object> output = new HashMap<>();
//        output.put("fac", 0.5f);
//
//        return output;
//    }
//}

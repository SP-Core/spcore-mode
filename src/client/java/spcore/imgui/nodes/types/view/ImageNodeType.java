package spcore.imgui.nodes.types.view;

import org.joml.Vector2f;
import org.joml.Vector4f;
import spcore.imgui.nodes.enums.NodeType;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.inputs.Vector4ValueInput;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.NodeInfo;
import spcore.imgui.nodes.models.PinInfo;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.imgui.nodes.types.AbstractNodeType;
import spcore.view.ViewComponent;
import spcore.view.components.ImageComponent;
import spcore.view.render.Renderable;

import java.util.HashMap;

public class ImageNodeType extends AbstractNodeType {
    @Override
    protected NodeInfo internalCreateInfo(NodeType nt) {
        var node = new NodeInfo(nt);

        node.addInput(new PinInfo("parent", PinType.Component));
        node.addInput(new PinInfo("width", PinType.Float));
        node.addInput(new PinInfo("height", PinType.Float));
        node.addInput(new PinInfo("position", PinType.Vector2));
        node.addInput(new PinInfo("regular", PinType.Bool));
        node.addInput(new PinInfo("src", PinType.String));

        node.addOutput(new PinInfo("component", PinType.Component));
        return node;
    }

    @Override
    protected HashMap<String, Object> internalProcess(Node node, ProcessService inputs) {
        HashMap<String, Object> outputs = new HashMap<>();
        var view = new ImageComponent();
        if(inputs.contains("parent")){
            view.setParent((Renderable)inputs.process("parent"));
        }

        if(inputs.contains("width")){
            var w = inputs.process("width");
            if(w instanceof Float f){
                view.setWidth(f);
            }
            else{
                view.setWidth((int)w);
            }
        }
        else if(node.containsInputValue("width")){
            view.setWidth(Float.parseFloat(node.getInputValue("width")));
        }

        if(inputs.contains("height")){
            var h = inputs.process("height");
            if(h instanceof Float f){
                view.setHeight(f);
            }
            else{
                view.setHeight((int)h);
            }
        }
        else if(node.containsInputValue("height")){
            view.setHeight(Float.parseFloat(node.getInputValue("height")));
        }

        if(inputs.contains("position")){
            var vector2 = (Vector2f)inputs.process("position");

            var xs = Float.toString(vector2.x);
            var ys = Float.toString(vector2.y);
            if(node.containsInputValue("regular")){
                if(!Boolean.parseBoolean(node.getInputValue("regular"))){
                    xs = xs + "%";
                    ys = ys + "%";
                }
            }

            view.setX(xs);
            view.setY(ys);

        }



        if(inputs.contains("src")){
            var src = (String)inputs.process("src");

            view.getStyle().styles.put(ImageComponent.ViewStyles.SRC, src);
        }
        else if(node.containsInputValue("src")){
            var src = node.getInputValue("src");

            view.getStyle().styles.put(ImageComponent.ViewStyles.SRC, src);
        }

        outputs.put("component", view);
        return outputs;
    }
}

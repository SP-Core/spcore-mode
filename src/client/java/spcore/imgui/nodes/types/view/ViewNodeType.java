package spcore.imgui.nodes.types.view;

import imgui.ImColor;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;
import spcore.imgui.nodes.NodeContext;
import spcore.imgui.nodes.enums.NodeType;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.inputs.Vector4ValueInput;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.NodeInfo;
import spcore.imgui.nodes.models.Pin;
import spcore.imgui.nodes.models.PinInfo;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.imgui.nodes.types.AbstractNodeType;
import spcore.view.ViewComponent;
import spcore.view.components.BorderableComponent;
import spcore.view.render.Renderable;

import java.util.HashMap;

public class ViewNodeType extends AbstractNodeType {

    @Override
    public NodeInfo internalCreateInfo(NodeType nt) {
        var node = new NodeInfo(nt);

        node.addInput(new PinInfo("parent", PinType.Component));
        node.addInput(new PinInfo("width", PinType.Float));
        node.addInput(new PinInfo("height", PinType.Float));
        node.addInput(new PinInfo("position", PinType.Vector2));
        node.addInput(new PinInfo("regular", PinType.Bool));
        node.addInput(new PinInfo("position-rotate", PinType.Float));
        node.addInput(new PinInfo("position-lock-x", PinType.Bool));
        node.addInput(new PinInfo("position-lock-y", PinType.Bool));
        node.addInput(new PinInfo("background-color", PinType.Vector4));

        node.addOutput(new PinInfo("component", PinType.Component));
        return node;
    }

    public ViewComponent createComponent(){
        return new ViewComponent();
    }

    @Override
    public HashMap<String, Object> internalProcess(Node node, ProcessService inputs) {
        HashMap<String, Object> outputs = new HashMap<>();
        var view = createComponent();
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


        if(inputs.contains("position-rotate")){
            view.setRotate((float)inputs.process("position-rotate"));
        }
        else if(node.containsInputValue("position-rotate")){
            view.setRotate(Float.parseFloat(node.getInputValue("position-rotate")));
        }

        if(inputs.contains("position-lock-x")){

            view.getStyle()
                    .styles.put(ViewComponent.ViewStyles.ROTATE_LOCK_X,
                            (String) inputs.process("position-lock-x"));

        }
        else if(node.containsInputValue("position-lock-x")){
            view.getStyle()
                    .styles.put(ViewComponent.ViewStyles.ROTATE_LOCK_X,
                            node.getInputValue("position-lock-x"));

        }

        if(inputs.contains("position-lock-y")){
            view.getStyle()
                    .styles.put(ViewComponent.ViewStyles.ROTATE_LOCK_Y,
                            (String) inputs.process("position-lock-y"));
        }
        else if(node.containsInputValue("position-lock-y")){
            view.getStyle()
                    .styles.put(ViewComponent.ViewStyles.ROTATE_LOCK_Y,
                            node.getInputValue("position-lock-y"));

        }

        if(inputs.contains("background-color")){
            var vector4 = (Vector4f)inputs.process("background-color");

            int red = (int) (vector4.x * 255);
            int green = (int) (vector4.y * 255);
            int blue = (int) (vector4.z * 255);
            int alpha = (int) (vector4.w * 255);

            int argb = (alpha << 24) | (red << 16) | (green << 8) | blue;

            String hexString = String.format("#%08X", argb);

            view.setBackground(hexString);
        }
        else if(node.containsInputValue("background-color")){
            var vector = Vector4ValueInput.parse(node.getInputValue("background-color"));
            int red = (int) (vector.x * 255);
            int green = (int) (vector.y * 255);
            int blue = (int) (vector.z * 255);
            int alpha = (int) (vector.w * 255);

            int argb = (alpha << 24) | (red << 16) | (green << 8) | blue;

            String hexString = String.format("#%08X", argb);

            view.setBackground(hexString);
        }

        outputs.put("component", view);
        return outputs;
    }
}


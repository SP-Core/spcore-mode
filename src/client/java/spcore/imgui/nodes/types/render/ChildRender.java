package spcore.imgui.nodes.types.render;

import spcore.fabric.screens.studio.windows.GuiNodesWindow;
import spcore.imgui.nodes.enums.NodeType;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.models.*;
import spcore.imgui.nodes.processor.ProcessService;
import spcore.imgui.nodes.types.AbstractNodeType;
import spcore.view.ViewComponent;

import java.util.HashMap;

public class ChildRender extends AbstractNodeType {
    @Override
    protected NodeInfo internalCreateInfo(NodeType nt) {
        var node = new NodeInfo(nt);
        node.addInput(new PinInfo("id", PinType.String));
        node.addInput(new PinInfo("width", PinType.Float));
        node.addInput(new PinInfo("height", PinType.Float));
        node.addInput(new PinInfo("inputs", PinType.Parameter));
        node.addOutput(new PinInfo("outputs", PinType.Parameter));
        node.addOutput(new PinInfo("component", PinType.Component));
        return node;
    }

    @Override
    protected HashMap<String, Object> internalProcess(Node node, ProcessService inputs) {

        HashMap<String, Object> outputs = new HashMap<>();
        if(!node.containsInputValue("id")){
            outputs.put("component", new ViewComponent());
            outputs.put("outputs", new HashMap<String, Object>());
            return outputs;
        }
        var id = node.getInputValue("id");

        var nodeContext = GuiNodesWindow.getOrCreateContext(id, inputs.render.context.scope);
        if(nodeContext == null){
            outputs.put("component", new ViewComponent());
            outputs.put("outputs", new HashMap<String, Object>());
            return outputs;
        }


        var scope = inputs.render.context.scope.copy();

        if(inputs.contains("width")){
            var w = inputs.process("width");
            if(w instanceof Float f){
                scope.width = f;
            }
            else{
                scope.width = ((int)w);
            }
        }
        else if(node.containsInputValue("width")){
            scope.width = Float.parseFloat(node.getInputValue("width"));
        }

        if(inputs.contains("height")){
            var h = inputs.process("height");
            if(h instanceof Float f){
                scope.height = f;
            }
            else{
                scope.height = ((int)h);
            }
        }
        else if(node.containsInputValue("height")){
            scope.height = Float.parseFloat(node.getInputValue("height"));
        }


        if(inputs.contains("inputs")){
            for (var p: inputs.processes("inputs", ParameterValue.class)
                 ) {
                scope.inputs.put(p.id, p.value);
            }
        }
        var view = nodeContext.startProcess(true, inputs.data, scope);

        outputs.put("outputs", view.getStyle().data);
        outputs.put("component", view);
        return outputs;
    }
}

package spcore.imgui.nodes.types;

import spcore.imgui.nodes.NodeContext;
import spcore.imgui.nodes.enums.NodeType;
import spcore.imgui.nodes.enums.PinKind;
import spcore.imgui.nodes.enums.PinType;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.NodeInfo;
import spcore.imgui.nodes.models.Pin;
import spcore.imgui.nodes.models.PinInfo;
import spcore.imgui.nodes.processor.ProcessService;

import java.util.HashMap;

public abstract class AbstractNodeType {

    protected abstract NodeInfo internalCreateInfo(NodeType nt);

    public NodeInfo createInfo(NodeType nt){
        var ni = this.internalCreateInfo(nt);
        var pin = new PinInfo("ts", PinType.TypeScriptSetter);
        pin.kind = PinKind.Input;
        ni.inputs.add(0, pin);
        return ni;
    }

    public Node create(NodeInfo nt, NodeContext context){
        var node = new Node(context.nextId(), nt.nodeType.name, nt.nodeType);
        for (var input: nt.inputs
             ) {
            node.inputs.add(new Pin(context.nextId(), input.name, input.type));
        }

        for (var output: nt.outputs
        ) {
            node.outputs.add(new Pin(context.nextId(), output.name, output.type));
        }

        return node;
    }

    protected abstract HashMap<String, Object> internalProcess
            (Node node, ProcessService inputs);

    public HashMap<String, Object> process(Node node, ProcessService inputs){
        if(inputs.contains("ts")){
            var id = (String)inputs.process("ts");
            if(inputs.render.context.setters.containsKey(id)){
                var setters = inputs.render.context.setters.get(id);

                for (var setter: setters
                     ) {
                    node.dynamicValues.put(setter.key, setter.value);
                }
            }
        }
        return this.internalProcess(node, inputs);
    }
}

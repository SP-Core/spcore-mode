package spcore.imgui.nodes.models;

import imgui.ImColor;
import imgui.ImVec2;
import spcore.imgui.nodes.enums.NodeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class Node {
    public NodeId id;
    public String name;
    public String nodeType;

    public List<Pin> inputs = new ArrayList<>();
    public List<Pin> outputs = new ArrayList<>();
    public HashMap<Integer, String> values = new HashMap<>();

    public transient NodeType innerType;
    public transient NodeInfo innerInfo;
    public transient Boolean init = false;

    public transient HashMap<String, String> dynamicValues = new HashMap<>();

    public Node(int id, String name, NodeType nodeType){
        this.id = new NodeId(id);
        this.name = name;
        this.innerType = nodeType;
        this.nodeType = nodeType.name();
    }

    public void init(){
        if(init != null && init){
            return;
        }
        init = true;
        this.dynamicValues = new HashMap<>();
        this.innerType = NodeType.valueOf(this.nodeType);
        this.innerInfo = NodeType.getNodeInfo(innerType);

        for (var i = 0; i < inputs.size(); i++) {
            var input = inputs.get(i);
            if(this.innerInfo.inputs.stream().anyMatch(p -> p.name.equals(input.pinName))){
                input.innerPin = this.innerInfo.inputs
                        .stream().filter(p -> p.name.equals(input.pinName))
                        .findFirst().get();
                continue;
            }
            inputs.remove(i);
        }

        for (var i = 0; i < outputs.size(); i++) {
            var input = outputs.get(i);
            if(this.innerInfo.outputs.stream().anyMatch(p -> p.name.equals(input.pinName))){
                input.innerPin = this.innerInfo.outputs
                        .stream().filter(p -> p.name.equals(input.pinName))
                        .findFirst().get();
                continue;
            }
            outputs.remove(i);
        }
    }

    public Pin getInputPinFromName(String name){
        return inputs.stream().filter(p -> p.pinName.equals(name)).findFirst().get();
    }

    public boolean containsInputValue(String name){
        if(dynamicValues.containsKey(name)){
            return true;
        }
        var pin = getInputPinFromName(name);
        return values.containsKey(pin.id.value);
    }

    public String getInputValue(String name){
        if(dynamicValues.containsKey(name)){
            return dynamicValues.get(name);
        }
        var pin = getInputPinFromName(name);
        return values.get(pin.id.value);
    }
}

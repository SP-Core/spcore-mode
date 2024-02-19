package spcore.imgui.nodes.processor;

import spcore.imgui.nodes.NodeRender;
import spcore.imgui.nodes.models.Link;
import spcore.imgui.nodes.models.Node;
import spcore.imgui.nodes.models.NodeId;

import java.util.*;

public class ProcessService {

//    public static HashMap<NodeId, HashMap<String, Object>> cache = new HashMap<>();
    public final NodeRender render;
    private final Node node;
    public final ProcessData data;

    public ProcessService(Node node, NodeRender render, ProcessData data) {
        this.render = render;
        this.node = node;
        this.data = data;
    }

    public Object process(String key){
        var pin = node.inputs.stream()
                .filter(p -> p.name.equals(key))
                .findFirst();

        if(pin.isEmpty()){
            return null;
        }

        var links = new ArrayList<>(render.context.links.stream()
                .filter(p -> p.startPinId.value == pin.get().id.value)
                .toList());

        links.sort(Comparator.comparingLong(Link::getCreate_time).reversed());

        if(links.stream().noneMatch(p -> true)){
            return null;
        }

        return processFromLink(links.get(0));
    }


    public <T> List<T> processes(String key, Class<T> clazz){
        var pin = node.inputs.stream()
                .filter(p -> p.name.equals(key))
                .findFirst()
                .get();

        var links = new ArrayList<>(render.context.links.stream()
                .filter(p -> p.startPinId.value == pin.id.value)
                .toList());

        links.sort(Comparator.comparingLong(Link::getCreate_time).reversed());


        List<T> results = new ArrayList<>();
        for (var link: links
             ) {
            var r = processFromLink(link);
            if(r == null){
                continue;
            }
            results.add((T)r);
        }

        return results;
    }

    private Object processFromLink(Link link){
        var childNode = render.context.nodes
                .stream()
                .filter(p -> p.outputs.stream().anyMatch(z -> z.id.value == link.endPinId.value))
                .findFirst();

        if(childNode.isEmpty()){
            return null;
        }

        var outPin = childNode.get().outputs
                .stream().filter(p -> p.id.value == link.endPinId.value)
                .findFirst()
                .get();

        var pType = render.nodeTypes
                .stream().filter(p -> p.getName().equals(childNode.get().name))
                .findFirst()
                .get();

        var ps = new ProcessService(childNode.get(), render, data);

        var outs = pType.process(childNode.get(), ps);
        return outs.get(outPin.name);
    }


    public boolean contains(String key){
        return process(key) != null;
    }

    public static class ProcessData{
        public final HashMap<NodeId, Random> randoms = new HashMap<>();

        public final HashMap<NodeId, HashMap<String, Object>> statics = new HashMap<>();

        public final HashMap<String, Integer> verticals = new HashMap<>();
        public final HashMap<String, Integer> horizontals = new HashMap<>();
    }
}


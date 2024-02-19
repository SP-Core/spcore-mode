package spcore.imgui.nodes;

import spcore.imgui.nextPinDelegate;
import spcore.view.ViewComponent;

import java.util.*;

public final class Graph {
    public int nextNodeId = 1;
    public int nextPinId = 100;

    public final Map<Integer, GraphNode> nodes = new HashMap<>();

    public Graph() {
        final GraphNode first = createGraphNode();
        final GraphNode second = createGraphNode();
        first.outputNodeId = second.nodeId;
        first.outputProperty = "background-color";
    }

    public GraphNode createGraphNode() {
        var node = GraphNode.createViewNode(() -> nextNodeId++);
        this.nodes.put(node.nodeId, node);
        return node;
    }

    public GraphNode findByInput(final long inputPinId) {
        for (GraphNode node : nodes.values()) {
            for (var d: node.inputPins.entrySet()
                 ) {
                if (d.getValue() == inputPinId) {
                    return node;
                }
            }

        }
        return null;
    }

    public GraphNode findByOutput(final long outputPinId) {
        for (GraphNode node : nodes.values()) {
            if (node.getOutputPinId() == outputPinId) {
                return node;
            }
        }
        return null;
    }

    public static final class GraphNode {
        public int nodeId;
        public final HashMap<String, Integer> inputPins = new HashMap<>();
        public final HashMap<Integer, String> inputProperties = new HashMap<>();

        public int outputPinId;

        public int outputNodeId = -1;
        public String outputProperty = null;
        public final String type;
        public final HashMap<String, String> properties = new HashMap<>();

        public GraphNode(String type) {
            this.type = type;
        }

        public int getInputPinId(String property) {
            return inputPins.get(property);
        }

        public int getOutputPinId() {
            return outputPinId;
        }

        public String getName() {
            return type + " " + (char) (64 + nodeId);
        }

        public static GraphNode createViewNode(nextPinDelegate nextPinDelegate){
            var view = new ViewComponent();
            var graph = new GraphNode("View");
            graph.nodeId = nextPinDelegate.nextPin();
            for (var style: view.getStyle().styles.entrySet()
                 ) {
                graph.properties.put(style.getKey(), style.getValue());
                var np = nextPinDelegate.nextPin();
                graph.inputProperties.put(np, style.getKey());
                graph.inputPins.put(style.getKey(), np);
            }
            graph.outputPinId = nextPinDelegate.nextPin();
            return graph;
        }
    }




}
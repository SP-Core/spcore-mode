package spcore.imgui.nodes;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.extension.nodeditor.NodeEditor;
import imgui.extension.nodeditor.NodeEditorConfig;
import imgui.extension.nodeditor.NodeEditorContext;
import imgui.extension.nodeditor.flag.NodeEditorPinKind;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiDockNodeFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImLong;
import spcore.imgui.ImGuiImpl;

import java.awt.*;
import java.net.URI;

public class NodeEditorRender {
    private static final NodeEditorContext CONTEXT;
    private static final String URL = "https://github.com/Nelarius/imnodes/tree/857cc86";
    static {
        NodeEditorConfig config = new NodeEditorConfig();
        config.setSettingsFile(null);
        CONTEXT = new NodeEditorContext(config);
    }

    public static void Render(ImGuiIO io, Graph graph){
//        ImGui.s
//        ImGui.setNextWindowSize(ImGui.getMainViewport().getSizeX(), ImGui.getMainViewport().getSizeY(), ImGuiCond.Once);
//        ImGui.setNextWindowPos(ImGui.getMainViewport().getPosX(), ImGui.getMainViewport().getPosY(), ImGuiCond.Once);
        if (ImGui.begin("SP.Core editor", ImGuiWindowFlags.NoTitleBar)) {
            ImGui.text("This a demo graph editor for imgui-node-editor");

            ImGui.alignTextToFramePadding();
            ImGui.text("Repo:");
            ImGui.sameLine();
            if (ImGui.button(URL)) {
                try {
                    Desktop.getDesktop().browse(new URI(URL));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }



            if (ImGui.button("Navigate to content")) {
                NodeEditor.navigateToContent(1);
            }

            NodeEditor.setCurrentEditor(CONTEXT);
            NodeEditor.begin("Node Editor");

            for (Graph.GraphNode node : graph.nodes.values()) {
                NodeEditor.beginNode(node.nodeId);

                ImGui.text(node.getName());


                for (var child: node.properties.entrySet()
                     ) {
                    NodeEditor.beginPin(node.getInputPinId(child.getKey()), NodeEditorPinKind.Input);
                    ImGui.text("-> " + child.getKey());
                    NodeEditor.endPin();
                }


                ImGui.sameLine();

                NodeEditor.beginPin(node.getOutputPinId(), NodeEditorPinKind.Output);
                ImGui.text("Out ->");
                NodeEditor.endPin();

                NodeEditor.endNode();
            }

            if (NodeEditor.beginCreate()) {
                final ImLong a = new ImLong();
                final ImLong b = new ImLong();
                if (NodeEditor.queryNewLink(a, b)) {
                    final Graph.GraphNode source = graph.findByOutput(a.get());
                    final Graph.GraphNode target = graph.findByInput(b.get());
                    if(source != null && target != null && NodeEditor.acceptNewItem()){
                        source.outputNodeId = target.nodeId;
                        source.outputProperty = target.inputProperties.get((int)b.get());

                    }
//                    if (source != null && target != null && ((int)b.get() != )source.outputNodeId != target.nodeId && NodeEditor.acceptNewItem()) {
//                        source.outputNodeId = target.nodeId;
//                        source.outputProperty = target.inputProperties.get((int)b.get());
//                    }
                }
            }
            NodeEditor.endCreate();

            int uniqueLinkId = 1;
            for (Graph.GraphNode node : graph.nodes.values()) {
                if (graph.nodes.containsKey(node.outputNodeId)) {
//                    String propName = null;
//                    for (var props: node.inputPins.entrySet()
//                         ) {
//                        if(props.getValue() == node.outputNodeId){
//                            propName = props.getKey();
//                        }
//                    }
//                    if(propName == null){
//                        continue;
//                    }

                    NodeEditor.link(uniqueLinkId++, node.getOutputPinId(), graph.nodes.get(node.outputNodeId).getInputPinId(node.outputProperty));
                }
            }

            NodeEditor.suspend();

            final long nodeWithContextMenu = NodeEditor.getNodeWithContextMenu();
            if (nodeWithContextMenu != -1) {
                ImGui.openPopup("node_context");
                ImGui.getStateStorage().setInt(ImGui.getID("delete_node_id"), (int) nodeWithContextMenu);
            }

            if (ImGui.isPopupOpen("node_context")) {
                final int targetNode = ImGui.getStateStorage().getInt(ImGui.getID("delete_node_id"));
                if (ImGui.beginPopup("node_context")) {
                    if (ImGui.button("Delete " + graph.nodes.get(targetNode).getName())) {
                        graph.nodes.remove(targetNode);
                        ImGui.closeCurrentPopup();
                    }
                    ImGui.endPopup();
                }
            }

            if (NodeEditor.showBackgroundContextMenu()) {
                ImGui.openPopup("node_editor_context");
            }

            if (ImGui.beginPopup("node_editor_context")) {
                if (ImGui.button("Create New Node")) {
                    final Graph.GraphNode node = graph.createGraphNode();
                    final float canvasX = NodeEditor.toCanvasX(ImGui.getMousePosX());
                    final float canvasY = NodeEditor.toCanvasY(ImGui.getMousePosY());
                    NodeEditor.setNodePosition(node.nodeId, canvasX, canvasY);
                    ImGui.closeCurrentPopup();
                }
                ImGui.endPopup();
            }

            NodeEditor.resume();
            NodeEditor.end();
        }
        ImGui.end();
    }
}

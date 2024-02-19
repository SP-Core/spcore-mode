package spcore.imgui.nodes.utils;

import spcore.imgui.nodes.models.NodeId;

public class NodeEditorUtils {

    public static boolean nodeIdLess(NodeId lhs, NodeId rhs){
        return lhs.value < rhs.value;
    }


}

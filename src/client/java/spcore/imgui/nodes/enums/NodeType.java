package spcore.imgui.nodes.enums;

public enum NodeType {
    Blueprint(0),
    Math(1);
    public final int value;
    private NodeType(int value){
        this.value = value;
    }
}

package spcore.imgui.nodes.models;

public class NodeId {
    public final int value;

    public NodeId(int value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return value;
    }
}

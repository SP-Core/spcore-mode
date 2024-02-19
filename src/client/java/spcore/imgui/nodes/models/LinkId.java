package spcore.imgui.nodes.models;

public class LinkId {
    public final int value;

    public LinkId(int value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return value;
    }
}

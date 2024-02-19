package spcore.imgui.nodes.models;

public class PinId {
    public final int value;
    public PinId(int value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        return this.hashCode() == obj.hashCode();
    }
}

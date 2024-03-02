package spcore.spnet.packets.outputs;

public enum ResourceModifier {
    None(0),
    Static(1),
    Dynamic(2);
    public final int value;

    ResourceModifier(int value) {
        this.value = value;
    }

    public static ResourceModifier fromInteger(int x) {
        return switch (x) {
            case 0 -> None;
            case 1 -> Static;
            case 2 -> Dynamic;
            default -> null;
        };
    }
}

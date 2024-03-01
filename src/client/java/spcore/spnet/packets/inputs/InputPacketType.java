package spcore.spnet.packets.inputs;

public enum InputPacketType {
    None(0),
    Resource(1),
    Sound(2);
    public final int value;

    InputPacketType(int value) {
        this.value = value;
    }

    public static InputPacketType fromInteger(int x) {
        return switch (x) {
            case 0 -> None;
            case 1 -> Resource;
            case 2 -> Sound;
            default -> null;
        };
    }
}

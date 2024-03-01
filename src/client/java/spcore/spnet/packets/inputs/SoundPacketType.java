package spcore.spnet.packets.inputs;

public enum SoundPacketType {
    None(0),
    Start(1),
    End(2);
    public final int value;

    SoundPacketType(int value) {
        this.value = value;
    }

    public static SoundPacketType fromInteger(int x) {
        return switch (x) {
            case 0 -> None;
            case 1 -> Start;
            case 2 -> End;
            default -> null;
        };
    }
}

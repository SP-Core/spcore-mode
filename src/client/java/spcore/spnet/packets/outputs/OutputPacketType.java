package spcore.spnet.packets.outputs;

import spcore.spnet.packets.inputs.SoundPacketType;

public enum OutputPacketType {
    None(0),
    UserPosition(1),
    Sound(2),
    Resource(3);
    public final int value;

    OutputPacketType(int value) {
        this.value = value;
    }

    public static OutputPacketType fromInteger(int x) {
        return switch (x) {
            case 0 -> None;
            case 1 -> UserPosition;
            case 2 -> Sound;
            case 3 -> Resource;
            default -> null;
        };
    }
}

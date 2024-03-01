package spcore.spnet.models;

import spcore.spnet.packets.inputs.SoundPacketType;

public enum ResourceType {
    None(0),
    Sound(1);
    public final int value;

    ResourceType(int value) {
        this.value = value;
    }

    public static ResourceType fromInteger(int x) {
        return switch (x) {
            case 0 -> None;
            case 1 -> Sound;
            default -> null;
        };
    }
}

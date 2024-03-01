package spcore.spnet.packets.inputs;

public class InputPacketsMap {
    public static AbstractInputPacket ResolvePacket(InputPacketType packetType)
    {
        return switch (packetType) {
            case Sound -> new SoundPacket();
            case Resource -> new ResourcePacket();
            default -> throw new RuntimeException();
        };

    }
}

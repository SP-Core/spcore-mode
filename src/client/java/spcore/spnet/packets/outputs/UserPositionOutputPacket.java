package spcore.spnet.packets.outputs;

import net.minecraft.util.math.Vec3d;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class UserPositionOutputPacket extends AbstractOutputPacket{
    public final int WorldId;
    public final Vec3d Position;
    public UserPositionOutputPacket(int worldId, Vec3d position) {
        super(OutputPacketType.UserPosition);

        WorldId = worldId;
        Position = position;
    }

    @Override
    protected void PacketWrite(DataOutputStream writer) throws IOException {
        writer.writeInt(s(WorldId));
        writer.writeInt(s((int)Position.x));
        writer.writeInt(s((int)Position.y));
        writer.writeInt(s((int)Position.z));
    }

    @Override
    public int GetHash() {
        return Objects.hash(WorldId, Position.x, Position.y, Position.z);
    }
}

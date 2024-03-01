package spcore.spnet.packets.outputs;

import spcore.spnet.models.ResourcePoint;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class ResourceOutputPacket extends AbstractOutputPacket {
    public final ResourcePoint Point;

    public ResourceOutputPacket(ResourcePoint point) {
        super(OutputPacketType.Resource);
        Point = point;
    }

    @Override
    protected void PacketWrite(DataOutputStream writer) throws IOException {
        writer.writeInt(Point.Id.Value);
        writer.writeInt(Point.Id.Type.value);
        writer.writeInt((int)Point.Position.x);
        writer.writeInt((int)Point.Position.y);
        writer.writeInt((int)Point.Position.z);
    }

    @Override
    public int GetHash() {
        return Objects.hash(Point.Id.Value, Point.Id.Type.value, Point.Position.x, Point.Position.y, Point.Position.z);
    }
}

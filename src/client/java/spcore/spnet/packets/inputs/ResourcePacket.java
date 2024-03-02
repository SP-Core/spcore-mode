package spcore.spnet.packets.inputs;

import net.minecraft.util.math.Vec3d;
import spcore.spnet.models.ResourceId;
import spcore.spnet.models.ResourcePoint;
import spcore.spnet.models.ResourceType;
import spcore.spnet.packets.outputs.ResourceModifier;

import java.io.DataInputStream;
import java.io.IOException;

public class ResourcePacket extends AbstractInputPacket{
    protected ResourcePacket() {
        super(InputPacketType.Resource);
    }

    public ResourcePoint Point;

    @Override
    public void PacketRead(DataInputStream reader) throws IOException {
        var id = s(reader.readInt());
        var type = ResourceType.fromInteger(s(reader.readInt()));
        var modifier = ResourceModifier.fromInteger(s(reader.readInt()));
        var x = s(reader.readInt());
        var y = s(reader.readInt());
        var z = s(reader.readInt());
        var vector = new Vec3d(x, y, z);
        Point = new ResourcePoint(new ResourceId(id, type), vector, modifier);
    }
}

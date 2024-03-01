package spcore.spnet.models;

import net.minecraft.util.math.Vec3d;

public class ResourcePoint {
    public final ResourceId Id;
    public final Vec3d Position;


    public ResourcePoint(ResourceId id, Vec3d position) {
        Id = id;
        Position = position;
    }
}

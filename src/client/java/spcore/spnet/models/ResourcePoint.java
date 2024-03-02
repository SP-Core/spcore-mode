package spcore.spnet.models;

import net.minecraft.util.math.Vec3d;
import spcore.spnet.packets.outputs.ResourceModifier;

public class ResourcePoint {
    public final ResourceId Id;
    public final Vec3d Position;
    public final ResourceModifier Modifier;

    public ResourcePoint(ResourceId id, Vec3d position, ResourceModifier modifier) {
        Id = id;
        Position = position;
        Modifier = modifier;
    }
}

package spcore.fabric.resources;

import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;

public interface ResourceProvider {

    int getSize();

    boolean contains(Identifier key);

    Resource get(Identifier key);
}

package spcore.fabric.sounds;

import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class SpCoreResourceFactory implements ResourceFactory {
    @Override
    public Optional<Resource> getResource(Identifier id) {
        return Optional.empty();
    }
}

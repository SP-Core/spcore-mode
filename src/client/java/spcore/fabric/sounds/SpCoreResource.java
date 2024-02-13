package spcore.fabric.sounds;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.DirectoryResourcePack;
import net.minecraft.resource.InputSupplier;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.metadata.ResourceMetadata;

import java.io.InputStream;
import java.nio.file.Path;

public class SpCoreResource extends Resource {
    public SpCoreResource(InputSupplier<InputStream> inputSupplier) {
        super(new DirectoryResourcePack("spcore", Path.of("spcore"), true), inputSupplier);
    }


}

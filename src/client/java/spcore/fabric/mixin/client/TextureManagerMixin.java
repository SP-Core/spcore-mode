package spcore.fabric.mixin.client;

import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureManager.class)
public class TextureManagerMixin {
    @Mutable
    @Final
    @Shadow
    private ResourceManager resourceContainer;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void findSounds(ResourceManager resourceContainer, CallbackInfo ci) {

//        this.resourceContainer = TestResourceProvider.createManager(resourceContainer);
    }
}

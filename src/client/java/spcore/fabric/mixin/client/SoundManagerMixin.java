package spcore.fabric.mixin.client;

import net.minecraft.client.option.GameOptions;
import net.minecraft.client.sound.SoundLoader;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import spcore.fabric.resources.SpCoreResourceMap;
import spcore.fabric.resources.TestResourceProvider;

import java.util.Map;

@Mixin(SoundLoader.class)
public class SoundManagerMixin {

    @Mutable
    @Final
    @Shadow
    private ResourceFactory resourceFactory;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void findSounds(ResourceFactory resourceFactory, CallbackInfo ci) {
        this.resourceFactory = TestResourceProvider.createFactory(resourceFactory);
    }
}

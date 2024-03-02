package spcore.fabric.mixin.client;


import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import spcore.GlobalContext;
import spcore.appapi.background.BackgroundJobManager;
import spcore.engine.AppEngine;
import spcore.fabric.sounds.SpCoreSound;
import spcore.spnet.SpNetClient;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    @Final
    private Window window;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void initImGui(RunArgs args, CallbackInfo ci) {
        var app = new AppEngine(window);
        app.init();
        BackgroundJobManager.getInstance();
    }

    @Final
    @Shadow
    private LevelStorage levelStorage;

    @Inject(at = @At("HEAD"), method = "disconnect*")
    private void disconnect(Screen screen, CallbackInfo info) {
        if (levelStorage != null) {
            try {
                SpNetClient.getInstance().stop();
            } catch (Exception e) {
                GlobalContext.LOGGER.error(e.getMessage());
            }
        }
    }
}

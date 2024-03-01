package spcore.fabric.mixin.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import spcore.fabric.screens.studio.InGameHudCoreContext;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "render", at = @At("RETURN"))
    private void render(DrawContext drawContext, float time, CallbackInfo info) {
        if(InGameHudCoreContext.handler != null){
            InGameHudCoreContext.handler.render(drawContext, time);
        }
    }
}

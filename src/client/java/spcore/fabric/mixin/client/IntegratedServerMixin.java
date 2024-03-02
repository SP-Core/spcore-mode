package spcore.fabric.mixin.client;

import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import spcore.spnet.SpNetClient;

import javax.annotation.Nullable;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {
    @Inject(method = "openToLan", at = @At(value = "RETURN"))
    public void publishServer(@Nullable GameMode gameType, boolean cheats, int port, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (!callbackInfo.getReturnValue()) {
            return;
        }
        SpNetClient.getInstance().start();
    }
}

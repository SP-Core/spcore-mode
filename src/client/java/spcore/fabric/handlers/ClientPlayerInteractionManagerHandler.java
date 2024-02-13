package spcore.fabric.handlers;

import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundLoader;
import net.minecraft.sound.SoundEvent;
import spcore.GlobalContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ClientPlayerInteractionManagerHandler {

    public static void onClickSlot (int syncId, int slotId, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci){
        GlobalContext.LOGGER.info(syncId + " " + slotId + " " + button + " " + actionType + " " +player.currentScreenHandler.getClass());
        assert MinecraftClient.getInstance().player != null;
        SharedConstants.isDevelopment = true;


        MinecraftClient.getInstance().player.playSound(TerminalHandler.Sound, 1, 1);
        SharedConstants.isDevelopment = false;
    }
}

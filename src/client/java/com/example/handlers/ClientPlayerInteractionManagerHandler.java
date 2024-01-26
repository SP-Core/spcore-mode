package com.example.handlers;

import com.example.SpCore;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ClientPlayerInteractionManagerHandler {

    public static void onClickSlot (int syncId, int slotId, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci){
        SpCore.LOGGER.info(syncId + " " + slotId + " " + button + " " + actionType + " " +player.currentScreenHandler.getClass());
    }
}

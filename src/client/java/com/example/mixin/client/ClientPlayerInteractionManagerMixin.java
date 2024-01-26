package com.example.mixin.client;

import com.example.handlers.ClientPlayerInteractionManagerHandler;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {


	@Inject(method = "clickSlot", at = @At("RETURN"))
	public void onClickSlot (int syncId, int slotId, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci){
		ClientPlayerInteractionManagerHandler.onClickSlot(syncId, slotId, button, actionType, player, ci);
	}
}
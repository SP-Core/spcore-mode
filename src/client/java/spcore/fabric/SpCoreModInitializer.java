package spcore.fabric;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import spcore.GlobalContext;
import spcore.MixinHandlers;
import spcore.api.AuthContext;
import spcore.fabric.eventHandlers.ShopCardHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import spcore.fabric.eventHandlers.TreeHandler;
import spcore.fabric.handlers.TerminalHandler;

public class SpCoreModInitializer implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		GlobalContext.LOGGER.info("CLIENT INIT");

 		UseBlockCallback.EVENT.register(TreeHandler::Invoke);
		UseBlockCallback.EVENT.register(ShopCardHandler::Invoke);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {

			if(GlobalContext.getIsInit()){
				return;
			}

			if(client.player == null){
				return;
			}

			GlobalContext.Init(client.player);
			AuthContext.Init();
		});
	}

}
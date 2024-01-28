package spcore.fabric;

import spcore.GlobalContext;
import spcore.api.AuthContext;
import spcore.fabric.eventHandlers.ShopCardHandler;
import spcore.fabric.eventHandlers.TreeHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;

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
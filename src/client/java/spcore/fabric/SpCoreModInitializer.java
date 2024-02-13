package spcore.fabric;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import spcore.GlobalContext;
import spcore.MixinHandlers;
import spcore.api.AuthContext;
import spcore.appapi.background.BackgroundJobManager;
import spcore.appapi.configuration.KnowApplicationManager;
import spcore.appapi.helpers.PathHelper;
import spcore.engine.AppEngine;
import spcore.fabric.eventHandlers.ShopCardHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import spcore.fabric.eventHandlers.TreeHandler;
import spcore.fabric.handlers.TerminalHandler;
import spcore.fabric.screens.AppResolverScreen;

import javax.script.ScriptException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SpCoreModInitializer implements ClientModInitializer {

	private static Screen showCurrentScreen;
	@Override
	public void onInitializeClient() {
		GlobalContext.LOGGER.info("CLIENT INIT");
		SharedConstants.isDevelopment = true;
		TerminalHandler.Sound = Registry.register(Registries.SOUND_EVENT, TerminalHandler.Id, SoundEvent.of(TerminalHandler.Id));
		//UseBlockCallback.EVENT.register(TreeHandler::Invoke);
		UseBlockCallback.EVENT.register(ShopCardHandler::Invoke);
		KnowApplicationManager.addResolver(p -> {
			if(AppResolverScreen.CurrentAppHash == p.hashCode() && AppResolverScreen.Resolve){
				return true;
			}
			showCurrentScreen = new AppResolverScreen(p);
			return false;
		});

		KnowApplicationManager.addRunner(p -> {
			var oldApps = KnowApplicationManager.getApplications()
					.stream().filter(pp -> {
						return pp.Manifest.manifest.appId.equals(p.manifest.appId) &&
								pp.Hash != p.hashCode();
					});
			for (var oldApp: oldApps.toList()
				 ) {
				KnowApplicationManager.notTrust(oldApp.Manifest);
			}
		});
		KnowApplicationManager.addRunner(p -> {
			try {
				var disableBackground =
						KnowApplicationManager.getApplications()
								.stream().anyMatch(z -> z.Hash == p.hashCode() && z.DisableBackground);

			   	var runtime = BackgroundJobManager.getInstance().register(p, disableBackground);
				AppEngine.getInstance().runApp(runtime, p);
			} catch (ScriptException e) {
				GlobalContext.LOGGER.error(e.getMessage());
			}
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {

			if(showCurrentScreen != null){
				MinecraftClient.getInstance().setScreen(showCurrentScreen);
				showCurrentScreen = null;
			}
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
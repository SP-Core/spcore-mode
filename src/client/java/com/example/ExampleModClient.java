package com.example;

import com.example.eventHandlers.ShopCardHandler;
import com.example.eventHandlers.TreeHandler;
import com.example.spcore.SpCoreApiContext;
import com.example.spcore.SpCoreTransaction;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.block.AzaleaBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import okhttp3.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import static net.minecraft.block.SaplingBlock.STAGE;

public class ExampleModClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		SpCore.LOGGER.info("CLIENT INIT");

		UseBlockCallback.EVENT.register(TreeHandler::Invoke);
		UseBlockCallback.EVENT.register(ShopCardHandler::Invoke);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if(SpCoreApiContext.IsInit){
				return;
			}

			if(client.player == null){
				return;
			}

			SpCoreApiContext.UserName = "Dima5x9";
			SpCoreApiContext.Uuid = client.player.getUuidAsString();
			SpCoreApiContext.IsInit = true;
		});
	}

}
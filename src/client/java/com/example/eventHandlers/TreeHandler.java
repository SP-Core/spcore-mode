package com.example.eventHandlers;

import com.example.spcore.SpCoreTransaction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import okhttp3.*;

import java.io.IOException;
import java.util.Objects;

import static net.minecraft.block.SaplingBlock.STAGE;

public class TreeHandler {
    private static final Object lock = new Object();

    private static SpCoreTransaction ltransaction;
    private static int lastNote = 0;

    public static ActionResult Invoke(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult){
        ItemStack itemStack = player.getStackInHand(hand);
        if(itemStack.isEmpty() || !(itemStack.getItem() instanceof BoneMealItem)){
            return ActionResult.PASS;
        }

        BlockPos blockPos = hitResult.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        Integer stage = -1;
        SpCoreTransaction transaction = null;
        if(block instanceof SaplingBlock saplingBlock){
            stage = (Integer)blockState.get(STAGE);
            transaction = new SpCoreTransaction(saplingBlock.getTranslationKey(), blockPos);
            MinecraftClient mc = MinecraftClient.getInstance();
            if(ltransaction == null || !Objects.equals(ltransaction.toMessage(), transaction.toMessage())){
                ltransaction = transaction;
                lastNote = 0;
            }
            else{
                lastNote++;
            }
            player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BELL.value(), 1, getPitchFromNote(lastNote));
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.spcore.ru/api/tree/click-v0?token=" + player.getUuidAsString() + "&message=" + transaction.toMessage() + "&state=" + stage)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    // Обработка ошибки
                    //e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    if(!response.isSuccessful()){
                        return;
                    }
                    String responseBody = response.body().string();
                    response.close();
                    MinecraftClient mc = MinecraftClient.getInstance();
                    mc.inGameHud.setOverlayMessage(Text.of(responseBody), false);

                }
            });
        }
        return ActionResult.PASS;
    }

    public static float getPitchFromNote(int p_277409_) {
        return (float)Math.pow(2.0D, (double)(p_277409_ - 12) / 12.0D);
    }
}

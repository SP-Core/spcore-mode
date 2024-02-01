package spcore.fabric.eventHandlers;

import spcore.api.AuthContext;
import spcore.api.SpCoreApi;
import spcore.api.models.TreeTransaction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

import static net.minecraft.block.SaplingBlock.STAGE;

public class TreeHandler {
    private static final Object lock = new Object();

    private static TreeTransaction ltransaction;
    private static int lastNote = 0;

    public static ActionResult Invoke(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult){

        if(!AuthContext.isIsAuthorized()){
            return ActionResult.PASS;
        }

        ItemStack itemStack = player.getStackInHand(hand);
        if(itemStack.isEmpty() || !(itemStack.getItem() instanceof BoneMealItem)){
            return ActionResult.PASS;
        }

        BlockPos blockPos = hitResult.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        Integer stage = -1;
        TreeTransaction transaction = null;
        if(block instanceof SaplingBlock saplingBlock){
            stage = (Integer)blockState.get(STAGE);
            transaction = new TreeTransaction(saplingBlock.getTranslationKey(), blockPos);
            MinecraftClient mc = MinecraftClient.getInstance();
            if(ltransaction == null || !Objects.equals(ltransaction.toMessage(), transaction.toMessage())){
                ltransaction = transaction;
                lastNote = 0;
            }
            else{
                lastNote++;
            }
            player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BELL.value(), 1, getPitchFromNote(lastNote));

            SpCoreApi.TREE.TreeClick(transaction.toMessage(), stage);

        }
        return ActionResult.PASS;
    }

    public static float getPitchFromNote(int p_277409_) {
        return (float)Math.pow(2.0D, (double)(p_277409_ - 12) / 12.0D);
    }
}

package com.example.eventHandlers;

import com.example.models.SPCoreCard;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public class ShopCardHandler {

    public static ActionResult Invoke(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult){
        ItemStack itemStack = player.getStackInHand(hand);
        if(itemStack.isEmpty() || !(itemStack.getItem() instanceof WrittenBookItem)){
            return ActionResult.PASS;
        }

        var card = SPCoreCard.Parse(itemStack);
        return ActionResult.PASS;
    }
}

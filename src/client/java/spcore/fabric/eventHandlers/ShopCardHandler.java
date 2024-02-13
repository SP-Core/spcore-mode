package spcore.fabric.eventHandlers;

import net.minecraft.client.util.MacWindowUtil;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import spcore.fabric.models.SPCoreCard;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

package spcore.fabric.handlers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemFrameItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import spcore.fabric.models.SPCoreCard;
import spcore.spnet.SpNetClient;
import spcore.spnet.models.ResourceId;
import spcore.spnet.models.ResourceType;
import spcore.spnet.packets.outputs.ResourceModifier;

public class WrittenBookHandler {

    public static Integer lastSoundId = null;
    public static Integer lastFrameSoundId = null;
    private static final Object _lock = new Object();
    public static void handle(boolean fromPosition){
        var player = MinecraftClient.getInstance().player;
        var send = true;
        try {
            if(player == null){
                return;
            }

            var stack = player.getInventory().getMainHandStack();
            if(stack == null || stack.isEmpty()){
                return;
            }


            if(stack.getItem() == Items.WRITTEN_BOOK){
                var name = stack.getName().getString();
                if(name.startsWith("♬sound-lab ")){
                    var id = name.replaceAll("♬sound-lab ", "");
                    var idInt = Integer.parseInt(id);
                    if(lastFrameSoundId != null && lastFrameSoundId == idInt){
                        send = false;
                        return;
                    }
                    if(fromPosition){
                        SpNetClient.getInstance().setResourcePosition(new ResourceId(idInt, ResourceType.Sound), player.getPos(), ResourceModifier.Dynamic);
                    }
                    else{
                        if(lastSoundId == null){
                            lastSoundId = idInt;
                            SpNetClient.getInstance().startSound(idInt);
                            send = false;
                            return;
                        }
                        if(lastSoundId == idInt){
                            SpNetClient.getInstance().setResourcePosition(new ResourceId(idInt, ResourceType.Sound), player.getPos(), ResourceModifier.Dynamic);
                            send = false;
                            return;
                        }
                    }

                }
                else{
                    lastFrameSoundId = null;
                }
            }
            else{
                lastFrameSoundId = null;
            }
        }
        finally {
            if(send && !fromPosition){
                synchronized (_lock){
                    if(lastSoundId != null){
                        SpNetClient.getInstance().endSound(lastSoundId);
                        lastSoundId = null;
                    }
                }

            }
        }



    }

    public static ActionResult Invoke(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult){
        ItemStack itemStack = player.getStackInHand(hand);
        if(itemStack.isEmpty() || !(itemStack.getItem() instanceof WrittenBookItem)){
            return ActionResult.PASS;
        }

        if(!(entity instanceof ItemFrameEntity)){
            return ActionResult.PASS;
        }
        var name = itemStack.getName().getString();
        if(name.startsWith("♬sound-lab ")){
            var id = name.replaceAll("♬sound-lab ", "");
            var idInt = Integer.parseInt(id);
            synchronized (_lock){
                lastSoundId = null;
                lastFrameSoundId = idInt;
                SpNetClient.getInstance().startSound(idInt);
                SpNetClient.getInstance().setResourcePosition(new ResourceId(idInt, ResourceType.Sound), entity.getPos(), ResourceModifier.Static);
            }

        }


        return ActionResult.PASS;
    }
}

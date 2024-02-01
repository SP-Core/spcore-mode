package spcore;

import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import spcore.api.SpCoreApi;

import java.util.UUID;

public class MixinHandlers {

    public static void RemoveNbtTerminal(ItemStack itemStack){
        itemStack.setSubNbt("pages", new NbtList());
    }
}

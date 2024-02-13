package spcore.fabric.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import spcore.api.AuthContext;
import spcore.fabric.handlers.TerminalHandler;
import spcore.fabric.sounds.SpCoreSound;

import java.util.ArrayList;
import java.util.List;

@Mixin(BookScreen.class)
public class BookScreenMixin {

    @Shadow
    private BookScreen.Contents contents;

    @Shadow
    private int pageIndex;

    @Inject(method = "init", at = @At("RETURN"))
    public void onInit (CallbackInfo ci){
        if(!AuthContext.isIsAuthorized())
            return;
        var handler = new TerminalHandler();
        var mc = MinecraftClient.getInstance();
        List<String> l = new ArrayList<>();
        l.add(contents.getPage(pageIndex).getString());
        handler.invoke(mc.player, null, null, 0, l);


    }
}

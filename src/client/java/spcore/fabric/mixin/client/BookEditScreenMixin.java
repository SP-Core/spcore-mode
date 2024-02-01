package spcore.fabric.mixin.client;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.client.util.SelectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import spcore.api.AuthContext;
import spcore.fabric.handlers.TerminalHandler;

import java.util.List;

@Mixin(BookEditScreen.class)
public class BookEditScreenMixin {

    @Shadow
    private List<String> pages;

    @Shadow
    private SelectionManager currentPageSelectionManager;

    @Shadow
    private int currentPage;

    @Shadow
    private ItemStack itemStack;

    @Shadow
    private Hand hand;
    @Shadow
    private PlayerEntity player;
    @Inject(method = "keyPressedEditMode", at = @At("RETURN"))
    public void onKeyPressedEditMode (int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> ci){

        if(!AuthContext.isIsAuthorized()){
            TerminalHandler.Authorized(pages.get(currentPage), currentPageSelectionManager);
            return;
        }

        var handler = new TerminalHandler();
        handler.invoke(player, itemStack, hand, currentPage, pages);
    }

    @Inject(method = "init", at = @At("RETURN"))
    public void onInit (CallbackInfo ci){
        if(!AuthContext.isIsAuthorized())
            return;
        var handler = new TerminalHandler();
        handler.invoke(player, itemStack, hand, currentPage, pages);
    }
}

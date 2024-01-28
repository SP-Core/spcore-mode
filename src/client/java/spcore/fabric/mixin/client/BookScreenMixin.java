package spcore.fabric.mixin.client;

import spcore.fabric.SpCore;
import spcore.fabric.commands.commandEngine.CommandEngine;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.client.util.SelectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BookEditScreen.class)
public class BookScreenMixin {

    @Shadow
    private List<String> pages;

    @Shadow
    private SelectionManager currentPageSelectionManager;

    @Shadow
    private int currentPage;

    @Inject(method = "keyPressedEditMode", at = @At("RETURN"))
    public void onKeyPressedEditMode (int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> ci){

        try{
            CommandEngine.CurrentPages = pages;
            CommandEngine.CurrentPageIndex = currentPage;
            var pageContent = pages.get(currentPage);

            CommandEngine.onKeyDown(keyCode, scanCode, modifiers, pageContent, currentPageSelectionManager);
        }
        catch (Exception e){
            SpCore.LOGGER.info(e.getMessage());
        }
    }
}

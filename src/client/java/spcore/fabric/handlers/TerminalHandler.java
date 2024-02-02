package spcore.fabric.handlers;

import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.c2s.play.BookUpdateC2SPacket;
import net.minecraft.util.Hand;
import spcore.GlobalContext;
import spcore.MixinHandlers;
import spcore.api.AuthContext;
import spcore.api.SpCoreApi;
import spcore.api.helpers.SpCryptoLink;
import spcore.fabric.screens.TerminalScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class TerminalHandler {


    public void invoke(PlayerEntity player, ItemStack itemStack, Hand hand, int currentPageIndex, List<String> pages){
        NbtCompound nbt = null;
        if(itemStack != null){
            nbt = itemStack.getNbt();
        }

        if (nbt != null && pages == null) {
            pages = new ArrayList<>();
            Objects.requireNonNull(pages);
            BookScreen.filterPages(nbt, pages::add);
        }

        if(pages.isEmpty()){
            return;
        }

        final var lPages = pages;
        var mc = MinecraftClient.getInstance();
        int i = hand == Hand.MAIN_HAND ? player.getInventory().selectedSlot : 40;

        if(!pages.isEmpty()){
            var currentPage = pages.get(currentPageIndex);
            if(currentPage.startsWith("spt\n")){


                mc.setScreen(new TerminalScreen(mc.player, () ->{
                    mc.setScreen(null);
                }, t -> {
                    if(itemStack == null){
                        throw new Exception();
                    }
                    TimeUnit.SECONDS.sleep(1);
                    MixinHandlers.RemoveNbtTerminal(itemStack);
                    Objects.requireNonNull(mc.getNetworkHandler())
                            .sendPacket(new BookUpdateC2SPacket(i, lPages, Optional.of(t)));
                    mc.setScreen(null);
                }));
            }
        }



    }


    public static void Authorized(String page, SelectionManager selectionManager){
        var code = SpCryptoLink.parse(page);
        if(code == null){
            return;
        }
        AuthContext.setCode(code);
        SpCoreApi.AUTH.IsAuthorized(() -> {
            GlobalContext.LOGGER.info("Authorized success");
            selectionManager.selectAll();
            selectionManager.delete(-1);
            selectionManager.insert(GetSuccessAuthMessage());
            if(MinecraftClient.getInstance().player != null){
                SpCoreApi.APPS.MCProfiles(MinecraftClient.getInstance().player.getUuidAsString());
            }
        }, () -> {
            GlobalContext.LOGGER.info("Authorized error");
            selectionManager.selectAll();
            selectionManager.delete(-1);
            selectionManager.insert("Ошибка авторизации");
        });



    }

    private static String GetSuccessAuthMessage(){
        return "======SP-CORE======\n" +
                "Авторизация успешно пройдена!\n" +
                "\n" +
                "Для того чтобы открыть терминал, напишите \"spt\" в первой строке книги и нажмите Enter\n" +
                "  \n";
    }

}

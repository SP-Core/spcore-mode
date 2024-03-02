package spcore.fabric.handlers;

import com.google.common.collect.Lists;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.c2s.play.BookUpdateC2SPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import spcore.GlobalContext;
import spcore.MixinHandlers;
import spcore.api.AuthContext;
import spcore.api.SpCoreApi;
import spcore.api.helpers.SpCryptoLink;
import spcore.fabric.screens.TerminalScreen;
import spcore.fabric.screens.studio.StudioView;
import spcore.fabric.sounds.SpCoreSound;
import spcore.fabric.sounds.managers.SoundClient;
import spcore.fabric.sounds.models.ConnectionInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class TerminalHandler {


    public void invoke(PlayerEntity player, ItemStack itemStack, Hand hand, int currentPageIndex, List<String> pages){

//        var ff = new PositionedSoundInstance(SoundEvent.of(Id), SoundCategory.MASTER, 1, 1, Random.create(11), player.getX(), player.getY(), player.getZ());
//        MinecraftClient.getInstance()
//                        .getSoundManager().play(ff);
        //MinecraftClient.getInstance().setScreen(new StudioView("index2"));

//        assert MinecraftClient.getInstance().player != null;
//        SharedConstants.isDevelopment = true;
//
        //MinecraftClient.getInstance().player.playSound(SoundEvent.of(Id), 1, 1);
//        SharedConstants.isDevelopment = false;
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


                mc.setScreen(new TerminalScreen(() ->{
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

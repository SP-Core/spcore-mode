package spcore.api;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.network.ClientPlayerEntity;
import spcore.GlobalContext;
import spcore.fabric.SpCore;
import spcore.fabric.spcore.SpCoreApi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AuthContext {

    private static String Code;
    private static boolean IsAuthorized;

    public static String getCode() {
        return Code;
    }


    public static void Logout(){
        IsAuthorized = false;
        Code = null;
    }


    public static void Init(){
        Path secretPath = FabricLoader.getInstance().getGameDir().resolve(".secret");
        try {
            if(!Files.exists(secretPath)){
                return;
            }
            Code = Files.readString(secretPath);

            SpCoreApi.PostIsAuthorized(() -> {
                IsAuthorized = true;
                GlobalContext.LOGGER.info("Authorized success");
            }, () -> {
                GlobalContext.LOGGER.info("Authorized error");
                Code = null;
                IsAuthorized = false;
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void SaveSecret(){
        Path secretPath = FabricLoader.getInstance().getGameDir().resolve(".secret");
        try {
            Files.writeString(secretPath, Code);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isIsAuthorized() {
        return IsAuthorized;
    }

    public static void setIsAuthorized(boolean isAuthorized) {
        IsAuthorized = isAuthorized;
    }
}
